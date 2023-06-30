package overcloud.blog.application.role.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import overcloud.blog.application.user_role.core.UserRole;
import overcloud.blog.application.user_role.core.UserRoleId;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    @Query("SELECT user_role.id.roleId FROM UserRole user_role WHERE user_role.id.userId = :userId")
    List<UUID> findByUser(@Param("userId") UUID userId);
}
