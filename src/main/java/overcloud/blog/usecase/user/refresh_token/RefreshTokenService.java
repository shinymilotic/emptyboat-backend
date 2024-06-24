package overcloud.blog.usecase.user.refresh_token;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.entity.RefreshTokenEntity;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;
import overcloud.blog.usecase.common.auth.AuthResMsg;
import overcloud.blog.usecase.common.auth.service.JwtUtils;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.usecase.user.common.UserResponseMapper;

import java.util.Arrays;
import java.util.Optional;

@Service
public class RefreshTokenService {
    private final JwtUtils jwtUtils;
    private final JpaRefreshTokenRepository refreshTokenRepository;
    private final ResFactory resFactory;

    public RefreshTokenService(JwtUtils jwtUtils,
        JpaRefreshTokenRepository refreshTokenRepository,
        ResFactory resFactory) {
        this.resFactory = resFactory;
        this.jwtUtils = jwtUtils;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public RestResponse<RefreshTokenResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> refreshToken = readServletCookie(request, "refreshToken");

        if (refreshToken.isEmpty()) {
            throw new InvalidDataException(resFactory.fail(UserResMsg.REFRESHTOKEN_FAILED));
        }

        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken.get());

        if (refreshTokenEntity.isEmpty()) {
            throw new InvalidDataException(resFactory.fail(UserResMsg.REFRESHTOKEN_FAILED));
        }

        String gottenRefreshToken = refreshTokenEntity.get().getRefreshToken();
        try {
            jwtUtils.validateToken(gottenRefreshToken);
            String email = jwtUtils.getSub(refreshToken.get());
            String accessToken = jwtUtils.encode(email);
            Cookie jwtTokenCookie = new Cookie("jwtToken", accessToken);
            jwtTokenCookie.setMaxAge(86400);
            jwtTokenCookie.setSecure(false);
            jwtTokenCookie.setHttpOnly(true);
            jwtTokenCookie.setPath("/");
            jwtTokenCookie.setDomain("localhost");

            Cookie jwtRefreshTokenCookie = new Cookie("refreshToken", gottenRefreshToken);
            jwtRefreshTokenCookie.setMaxAge(86400);
            jwtRefreshTokenCookie.setSecure(false);
            jwtRefreshTokenCookie.setHttpOnly(true);
            jwtRefreshTokenCookie.setPath("/");
            jwtRefreshTokenCookie.setDomain("localhost");
            response.addCookie(jwtTokenCookie);
            response.addCookie(jwtRefreshTokenCookie);
            String userId = refreshTokenEntity.get().getUserId().toString();
            RefreshTokenResponse res = RefreshTokenResponse.builder().userId(userId).build();
            return resFactory.success(UserResMsg.REFRESHTOKEN_SUCCESS, res);
        } catch (JwtException e) {
            throw new InvalidDataException(resFactory.fail(UserResMsg.REFRESHTOKEN_FAILED));
        }
    }

    public Optional<String> readServletCookie(HttpServletRequest request, String name){
        return Arrays.stream(request.getCookies())
                .filter(cookie->name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
    }
}
