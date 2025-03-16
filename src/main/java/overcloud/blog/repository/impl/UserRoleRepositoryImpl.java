package overcloud.blog.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.UserRole;
import overcloud.blog.entity.UserRoleId;
import overcloud.blog.repository.UserRoleRepository;
import overcloud.blog.repository.jparepository.JpaUserRoleRepository;

import java.util.UUID;

@Repository
public class UserRoleRepositoryImpl implements UserRoleRepository {
    private final JpaUserRoleRepository jpa;
    private final EntityManager entityManager;

    public UserRoleRepositoryImpl(JpaUserRoleRepository jpa, EntityManager entityManager) {
        this.jpa = jpa;
        this.entityManager = entityManager;
    }

    public UserRole assignRole(String roleName, String email) {
        UUID userId = entityManager.createQuery("SELECT u.userId FROM UserEntity u WHERE u.email = :email ", UUID.class)
                .setParameter("email", email)
                .getSingleResult();

        UUID roleId = entityManager.createQuery("SELECT r.roleId FROM RoleEntity r WHERE r.name = :roleName", UUID.class)
                .setParameter("roleName", roleName)
                .getSingleResult();

        UserRole userRole = new UserRole(new UserRoleId(roleId, userId));
        return jpa.save(userRole);
    }

    @Override
    public UserRole saveAndFlush(UserRole userRole) {
        return jpa.saveAndFlush(userRole);
    }

    @Override
    public void delete(UserRole userRole) {
        jpa.delete(userRole);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        jpa.deleteByUserId(userId);
    }
}
