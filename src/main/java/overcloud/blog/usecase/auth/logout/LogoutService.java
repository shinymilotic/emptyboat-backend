package overcloud.blog.usecase.auth.logout;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.infrastructure.auth.bean.SecurityUser;
import overcloud.blog.infrastructure.cache.RedisUtils;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;

import java.util.Arrays;
import java.util.Optional;

@Service
public class LogoutService {

    private final JpaRefreshTokenRepository refreshTokenRepository;

    private final RedisUtils redisUtils;

    public LogoutService(JpaRefreshTokenRepository refreshTokenRepository, RedisUtils redisUtils) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.redisUtils = redisUtils;
    }

    @Transactional
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        readServletCookie(request, "refreshToken")
                .ifPresent(refreshTokenRepository::deleteByRefreshToken);

        Cookie jwtTokenCookie = new Cookie("jwtToken", null);
        jwtTokenCookie.setMaxAge(0);
        jwtTokenCookie.setSecure(false);
        jwtTokenCookie.setHttpOnly(true);
        jwtTokenCookie.setPath("/");
        jwtTokenCookie.setDomain("localhost");

        Cookie jwtRefreshTokenCookie = new Cookie("refreshToken", null);
        jwtRefreshTokenCookie.setMaxAge(0);
        jwtRefreshTokenCookie.setSecure(false);
        jwtRefreshTokenCookie.setHttpOnly(true);
        jwtRefreshTokenCookie.setPath("/");
        jwtRefreshTokenCookie.setDomain("localhost");

        response.addCookie(jwtTokenCookie);
        response.addCookie(jwtRefreshTokenCookie);
        return true;
    }

    public Optional<String> readServletCookie(HttpServletRequest request, String name){
        return Arrays.stream(request.getCookies())
                .filter(cookie->name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
    }
}
