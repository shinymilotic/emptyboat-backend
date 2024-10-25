package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
import overcloud.blog.entity.RoleEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaRoleRepository extends JpaRepository<RoleEntity, UUID> {
    @Query("SELECT r FROM RoleEntity r WHERE r.name IN (:roleNames)")
    List<RoleEntity> findAllByNames(@Param("roleNames") List<String> roleNames);

    @Modifying
    @Query("DELETE FROM RoleEntity r WHERE r.name = :deleteRoleName")
    int deleteRoleByName(@Param("deleteRoleName") String deleteRoleName);

    @Modifying
    @Query("UPDATE RoleEntity r SET r.name=:updateRoleName WHERE r.name=:currentRoleName")
    int updateRoleByName(@Param("currentRoleName") String currentRoleName, @Param("updateRoleName") String updateRoleName);
}
