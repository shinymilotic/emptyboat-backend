package overcloud.blog.repository;

import overcloud.blog.entity.UserEntity;
import overcloud.blog.usecase.user.common.UserResponse;
import overcloud.blog.usecase.user.get_profile.GetProfileResponse;
import java.util.List;
import java.util.UUID;

public interface UserRepository {
    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);
    UserEntity save(UserEntity entity);
    void enableUser(String confirmToken);
    List<UserResponse> getFollowers(UUID userId);
    List<UserEntity> findAll(int page, int size);
    UserEntity findRolesByUsernname(String username);
    GetProfileResponse findProfile(String username, UUID currentUserId);
    void deleteUser(UUID userId);
}
