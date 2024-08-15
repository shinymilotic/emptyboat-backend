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
    private UUID roleId;

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

        return getRoleId().equals(that.getRoleId());
    }

    @Override
    public int hashCode() {
        return getRoleId().hashCode();
    }
}
