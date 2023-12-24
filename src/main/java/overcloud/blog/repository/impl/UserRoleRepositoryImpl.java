package overcloud.blog.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.apache.catalina.User;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.RoleEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.entity.UserRole;
import overcloud.blog.entity.UserRoleId;
import overcloud.blog.repository.IUserRoleRepository;
import overcloud.blog.repository.jparepository.JpaUserRoleRepository;

import java.util.UUID;

@Repository
public class UserRoleRepositoryImpl implements IUserRoleRepository {

    private final JpaUserRoleRepository jpaUserRoleRepository;

    private final EntityManager entityManager;

    public UserRoleRepositoryImpl(JpaUserRoleRepository jpaUserRoleRepository, EntityManager entityManager) {
        this.jpaUserRoleRepository = jpaUserRoleRepository;
        this.entityManager = entityManager;
    }

    public void assignRole(String roleName, String email) {
        UUID userId = entityManager.createQuery("SELECT u.id FROM UserEntity u WHERE u.email = :email ",UUID.class)
                .setParameter("email", email)
                .getSingleResult();

        UUID roleId = entityManager.createQuery("SELECT r.id FROM RoleEntity r WHERE r.name = :roleName", UUID.class)
                .setParameter("roleName", roleName)
                .getSingleResult();

        UserRole userRole = UserRole.builder().id(new UserRoleId(roleId, userId))
                .role(RoleEntity.builder().id(roleId).build())
                .user(UserEntity.builder().id(userId).build())
                .build();
        jpaUserRoleRepository.save(userRole);
    }
}
