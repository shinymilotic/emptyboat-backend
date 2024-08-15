package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "refresh_tokens", schema = "public")
public class RefreshTokenEntity {
    @Id
    private UUID refreshTokenId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "user_id")
    private UUID userId;
}
