package overcloud.blog.application.user.refresh_token;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import overcloud.blog.infrastructure.security.service.JwtUtils;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final RedisTemplate<String, String> redisTemplate;
    private HashOperations<String, String, String> hashOperations;

    private final JwtUtils jwtUtils;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               RedisTemplate<String, String> redisTemplate,
                               JwtUtils jwtUtils) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.redisTemplate = redisTemplate;
        this.jwtUtils = jwtUtils;
    }

    @PostConstruct
    private void intializeHashOperations() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Transactional
    public String getRefreshToken(String expiredRefreshToken) {
        String email = jwtUtils.getSub(expiredRefreshToken);
        String newAccessToken = jwtUtils.encode(email);
        return newAccessToken;
    }
}
