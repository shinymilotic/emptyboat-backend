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

import java.util.Arrays;
import java.util.Optional;

@Service
public class RefreshTokenService {
    private final JwtUtils jwtUtils;

    private final JpaRefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(JwtUtils jwtUtils, JpaRefreshTokenRepository refreshTokenRepository) {
        this.jwtUtils = jwtUtils;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public boolean refreshToken(HttpServletRequest request, HttpServletResponse response) {

        Optional<String> refreshToken = readServletCookie(request, "refreshToken");
        Optional<RefreshTokenEntity> refreshTokenEntity = Optional.empty();
        if (refreshToken.isPresent()) {
            refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken.get());
            if (refreshTokenEntity.isPresent()) {
                String gottenRefreshToken = refreshTokenEntity.get().getRefreshToken();
                try {
                    jwtUtils.validateToken(gottenRefreshToken);
                } catch (JwtException e) {
                    throw new InvalidDataException(AuthError.AUTHORIZE_FAILED);
                }
            }

            String email = jwtUtils.getSub(refreshToken.get());
            String accessToken = jwtUtils.encode(email);

            Cookie jwtTokenCookie = new Cookie("jwtToken", accessToken);
            jwtTokenCookie.setMaxAge(86400);
            jwtTokenCookie.setSecure(false);
            jwtTokenCookie.setHttpOnly(true);
            jwtTokenCookie.setPath("/");
            jwtTokenCookie.setDomain("localhost");
            response.addCookie(jwtTokenCookie);

            return true;
        }


        return false;
    }

    public Optional<String> readServletCookie(HttpServletRequest request, String name){
        return Arrays.stream(request.getCookies())
                .filter(cookie->name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
    }
}
