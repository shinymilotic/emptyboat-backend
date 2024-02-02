package overcloud.blog.usecase.auth.refresh_token;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import overcloud.blog.entity.RoleEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@RedisHash
@Builder
public class RefreshTokenHash implements Serializable {
    @Id
    @ToString.Include
    private String id;

    private String email;

    private Set<RoleEntity> roles;

    private boolean enable;

    private LocalDateTime expiredTime;

    private LocalDateTime credentialsExpiredTime;
}
