package overcloud.blog.repository.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import overcloud.blog.entity.RoleEntity;
import overcloud.blog.repository.IRoleRepository;
import overcloud.blog.repository.jparepository.JpaRoleRepository;

@Repository
public class RoleRepositoryImpl implements IRoleRepository {

    private final JpaRoleRepository jpa;

    public RoleRepositoryImpl(JpaRoleRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<RoleEntity> findById(UUID fromString) {
        return jpa.findById(fromString);
    }

    @Override
    public RoleEntity saveAndFlush(RoleEntity roleEntity) {
        return jpa.saveAndFlush(roleEntity);
    }

    @Override
    public int updateRoleByName(String currentRoleName, String updateRoleName) {
        return this.jpa.updateRoleByName(currentRoleName, updateRoleName);
    }

    @Override
    public int deleteRoleByName(String deleteRoleName) {
        return this.jpa.deleteRoleByName(deleteRoleName);
    }
}
