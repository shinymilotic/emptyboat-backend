package overcloud.blog.usecase.auth.refresh_token;

import io.jsonwebtoken.JwtException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import overcloud.blog.entity.RefreshTokenEntity;
import overcloud.blog.infrastructure.auth.AuthError;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;
import overcloud.blog.infrastructure.auth.service.JwtUtils;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;

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
    public TokenRefreshResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        Optional<RefreshTokenEntity> refreshTokenEntity =  refreshTokenRepository.findByRefreshToken(refreshToken);

        if (refreshTokenEntity.isPresent()) {
            String gottenRefreshToken = refreshTokenEntity.get().getRefreshToken();
            try {
                jwtUtils.validateToken(gottenRefreshToken);
            } catch (JwtException e) {
                throw new InvalidDataException(AuthError.AUTHORIZE_FAILED);
            }
        }

        String email = jwtUtils.getSub(refreshToken);
        String accessToken = jwtUtils.encode(email);
        return new TokenRefreshResponse(accessToken);
    }
}
