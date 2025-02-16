package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.UserRole;
import overcloud.blog.entity.UserRoleId;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaUserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
//    @Query("SELECT user_role.id.roleId FROM UserRole user_role WHERE user_role.id.userId = :userId")
//    List<UUID> findByUser(@Param("userId") UUID userId);

    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.id.userId = :userId")
    void deleteByUserId(@Param("userId") UUID userId);
}
