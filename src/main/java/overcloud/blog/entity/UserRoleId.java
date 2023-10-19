package overcloud.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleId implements Serializable {
    @Column(name = "role_id")
    private UUID roleId;

    @Column(name = "user_id")
    private UUID userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRoleId that = (UserRoleId) o;

        if (!roleId.equals(that.roleId)) return false;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        int result = roleId.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }
}