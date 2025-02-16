package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "user_roles", schema = "public")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    @EmbeddedId
    private UserRoleId id;
}
