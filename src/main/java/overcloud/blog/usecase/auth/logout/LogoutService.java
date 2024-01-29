package overcloud.blog.usecase.auth.logout;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.infrastructure.cache.RedisUtils;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.usecase.auth.refresh_token.RefreshTokenRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {


    private final RedisUtils redisUtils;

    public LogoutService(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Transactional
    public boolean logout(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            SecurityUser user = (SecurityUser) auth.getPrincipal();
            if (user != null) {
                redisUtils.delete(user.getUser().getEmail());
            }
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return true;
    }
}
