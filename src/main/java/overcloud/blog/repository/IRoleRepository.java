package overcloud.blog.repository;

import java.util.Optional;
import java.util.UUID;

import overcloud.blog.entity.RoleEntity;

public interface IRoleRepository {

    Optional<RoleEntity> findById(UUID fromString);
}
