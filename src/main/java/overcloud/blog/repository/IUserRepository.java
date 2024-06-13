package overcloud.blog.repository;

import overcloud.blog.entity.UserEntity;
import overcloud.blog.usecase.auth.get_followers.FollowerListResposne;
import overcloud.blog.usecase.auth.get_followers.GetFollowers;

import java.util.List;
import java.util.UUID;

public interface IUserRepository {
    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);

    UserEntity save(UserEntity entity);

    void enableUser(String confirmToken);

    FollowerListResposne getFollowers(UUID userId);

    List<UserEntity> findAll(int page, int size);
}
