package overcloud.blog.usecase.auth.refresh_token;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import overcloud.blog.infrastructure.security.service.JwtUtils;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtUtils jwtUtils;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               JwtUtils jwtUtils) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public TokenRefreshResponse getRefreshToken(RefreshTokenRequest refreshTokenRequest) {
        String expiredRefreshToken = refreshTokenRequest.getRefreshToken();
        String email = jwtUtils.getSub(expiredRefreshToken);
        String accessToken = jwtUtils.encode(email);
        String refreshToken = jwtUtils.generateRefreshToken(email);
        refreshTokenRepository.deleteById(expiredRefreshToken);
        RefreshTokenHash refreshTokenHash = RefreshTokenHash.builder()
                .id(refreshToken)
                .email(email)
                .build();
        refreshTokenRepository.save(refreshTokenHash);
        return new TokenRefreshResponse(accessToken, refreshToken);
    }
}
