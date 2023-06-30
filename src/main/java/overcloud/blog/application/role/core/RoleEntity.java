package overcloud.blog.application.role.core;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user_role.core.UserRole;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles", schema = "public")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> user;

    @Override
    public String getAuthority() {
        return name;
    }
}
