package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

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

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleEntity that)) return false;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
