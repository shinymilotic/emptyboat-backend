package overcloud.blog.usecase.user.logout;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.util.Arrays;
import java.util.Optional;

@Service
public class LogoutService {

    private final JpaRefreshTokenRepository refreshTokenRepository;
    private final ResFactory resFactory;

    public LogoutService(JpaRefreshTokenRepository refreshTokenRepository, ResFactory resFactory) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.resFactory = resFactory;
    }

    @Transactional
    public RestResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
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
        
        return resFactory.success(UserResMsg.USER_LOGOUT_SUCCESS, null);
    }

    public Optional<String> readServletCookie(HttpServletRequest request, String name){
        return Arrays.stream(request.getCookies())
                .filter(cookie->name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
    }
}
