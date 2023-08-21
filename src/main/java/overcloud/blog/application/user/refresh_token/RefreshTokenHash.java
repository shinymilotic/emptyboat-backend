package overcloud.blog.application.user.refresh_token;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Data
@RedisHash("RefreshToken")
@Builder
public class RefreshTokenHash implements Serializable {
    @Id
    @ToString.Include
    private String id;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Indexed
    private String refreshToken;
}
