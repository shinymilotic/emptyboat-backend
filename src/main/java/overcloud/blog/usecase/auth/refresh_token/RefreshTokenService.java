package overcloud.blog.usecase.auth.refresh_token;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.RefreshTokenEntity;
import overcloud.blog.infrastructure.auth.AuthError;
import overcloud.blog.infrastructure.auth.service.JwtUtils;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;
import overcloud.blog.usecase.auth.common.UserResponseMapper;

import java.util.Arrays;
import java.util.Optional;

@Service
public class RefreshTokenService {
    private final JwtUtils jwtUtils;

    private final JpaRefreshTokenRepository refreshTokenRepository;

    private final UserResponseMapper userResponseMapper;


    public RefreshTokenService(JwtUtils jwtUtils, JpaRefreshTokenRepository refreshTokenRepository, UserResponseMapper userResponseMapper) {
        this.jwtUtils = jwtUtils;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userResponseMapper = userResponseMapper;
    }

    @Transactional
    public RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> refreshToken = readServletCookie(request, "refreshToken");
        RefreshTokenResponse emptyResponse = RefreshTokenResponse.builder().userId("").build();

        if (refreshToken.isEmpty()) {
            return emptyResponse;
        }

        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken.get());

        if (refreshTokenEntity.isEmpty()) {
            return emptyResponse;
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
            return RefreshTokenResponse.builder().userId(userId).build();
        } catch (JwtException e) {
            throw new InvalidDataException(AuthError.AUTHORIZE_FAILED);
        }
    }

    public Optional<String> readServletCookie(HttpServletRequest request, String name){
        return Arrays.stream(request.getCookies())
                .filter(cookie->name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
    }
}
