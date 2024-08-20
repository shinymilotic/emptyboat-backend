package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "public")
@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserEntity implements Serializable {
    @Id
    private UUID userId;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "bio")
    private String bio;

    @Column(name = "image")
    private String image;

    @ManyToMany
    @JoinTable(
            name = "follows",
            joinColumns = @JoinColumn(name = "followee_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<UserEntity> followers;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;

    @Column(name = "enable")
    private boolean enable;

    @Column(name = "expired_time")
    private LocalDateTime expiredTime;

    @Column(name = "credentials_expired_time")
    private LocalDateTime credentialsExpiredTime;

    public UserEntity() {
    }

    @Override
    public boolean equals(Object o) {
        UserEntity that = (UserEntity) o;

        if (that == null) {
            return false;
        }

        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
