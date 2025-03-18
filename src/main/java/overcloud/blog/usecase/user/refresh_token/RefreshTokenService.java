package overcloud.blog.usecase.user.refresh_token;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.auth.service.JwtUtils;
import overcloud.blog.repository.RefreshTokenRepository;
import overcloud.blog.entity.RefreshTokenEntity;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectsValidator validator;
    private final String REFRESH_TOKEN_FAILED = "Refresh token failed!";
    public RefreshTokenService(JwtUtils jwtUtils,
        RefreshTokenRepository refreshTokenRepository,
        ObjectsValidator validator) {
        this.validator = validator;
        this.jwtUtils = jwtUtils;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public UUID refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> refreshToken = readServletCookie(request, "refreshToken");

        if (refreshToken.isEmpty()) {
            throw validator.fail(REFRESH_TOKEN_FAILED);
        }

        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken.get());

        if (refreshTokenEntity.isEmpty()) {
            throw validator.fail(REFRESH_TOKEN_FAILED);
        }

        String gottenRefreshToken = refreshTokenEntity.get().getRefreshToken();
        boolean isValidate = jwtUtils.validateToken(gottenRefreshToken);

        if (!isValidate) {
            throw validator.fail(REFRESH_TOKEN_FAILED);
        }

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
        return refreshTokenEntity.get().getUserId();
    }

    public Optional<String> readServletCookie(HttpServletRequest request, String name){
        return Arrays.stream(request.getCookies())
                .filter(cookie->name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
    }
}
