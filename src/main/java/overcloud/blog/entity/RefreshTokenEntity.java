package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "refresh_token", schema = "public")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "user_id")
    private UUID userId;
}
