package overcloud.blog.usecase.user.login;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Data
@RedisHash
@Builder
public class UserHash {
    @Id
    @ToString.Include
    private String email;

    private UUID id;

    private String username;

    private String password;

    private String bio;

    private String image;

}
