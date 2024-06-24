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
}
