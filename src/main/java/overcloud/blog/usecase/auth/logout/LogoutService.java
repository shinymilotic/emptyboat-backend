package overcloud.blog.usecase.auth.logout;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import overcloud.blog.usecase.auth.refresh_token.RefreshTokenRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {

    private final RefreshTokenRepository refreshTokenRepository;

    public LogoutService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public boolean logout(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            refreshTokenRepository.deleteById(refreshToken);
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return true;
    }
}
