package overcloud.blog.application.user_role.core;

import jakarta.persistence.*;
import lombok.*;
import overcloud.blog.application.role.core.RoleEntity;
import overcloud.blog.application.user.core.UserEntity;

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
