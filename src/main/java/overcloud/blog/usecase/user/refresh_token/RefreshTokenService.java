package overcloud.blog.usecase.user.refresh_token;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.auth.service.JwtUtils;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
import overcloud.blog.entity.RefreshTokenEntity;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

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
    public RestResponse<UUID> refreshToken(HttpServletRequest request, HttpServletResponse response) {
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
            UUID userId = refreshTokenEntity.get().getUserId();
            return resFactory.success(UserResMsg.REFRESHTOKEN_SUCCESS, userId);
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
