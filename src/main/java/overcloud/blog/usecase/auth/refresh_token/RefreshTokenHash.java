package overcloud.blog.usecase.auth.refresh_token;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash
@Builder
public class RefreshTokenHash implements Serializable {
    @Id
    @ToString.Include
    private String id;

    private String email;
}
