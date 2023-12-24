package overcloud.blog.repository;

import overcloud.blog.entity.UserEntity;

public interface IUserRepository {
    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);

    UserEntity save(UserEntity entity);
}
