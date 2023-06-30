package overcloud.blog.application.user.core;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.application.article.favorite.core.FavoriteEntity;
import overcloud.blog.application.article_tag.core.ArticleTag;
import overcloud.blog.application.role.core.RoleEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "public")
@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "email")
    @NotNull
    @Email
    private String email;

    @Column(name = "username")
    @NotNull
    private String username;

    @Column(name = "password")
    @NotNull
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

    @OneToMany(mappedBy = "user")
    private List<FavoriteEntity> favorites;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
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

    @Override
    public boolean equals(Object o) {
        UserEntity that = (UserEntity) o;

        if(that == null) {
            return false;
        }

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public UserEntity() {
    }
}
