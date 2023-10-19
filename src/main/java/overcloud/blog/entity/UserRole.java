package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "user_role", schema = "public")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    @EmbeddedId
    private UserRoleId id;

    @ManyToOne
    @MapsId(value = "roleId")
    private RoleEntity role;

    @ManyToOne
    @MapsId(value = "userId")
    private UserEntity user;
}
