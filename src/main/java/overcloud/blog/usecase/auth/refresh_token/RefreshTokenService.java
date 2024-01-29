package overcloud.blog.usecase.auth.refresh_token;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import overcloud.blog.infrastructure.security.service.JwtUtils;

@Service
public class RefreshTokenService {
    private final JwtUtils jwtUtils;

    public RefreshTokenService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public TokenRefreshResponse getRefreshToken(RefreshTokenRequest refreshTokenRequest) {
        String expiredRefreshToken = refreshTokenRequest.getRefreshToken();
        String email = jwtUtils.getSub(expiredRefreshToken);
        String accessToken = jwtUtils.encode(email);
        String refreshToken = jwtUtils.generateRefreshToken(email);
        return new TokenRefreshResponse(accessToken, refreshToken);
    }
}
