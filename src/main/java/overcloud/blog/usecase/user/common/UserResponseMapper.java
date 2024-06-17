package overcloud.blog.usecase.user.common;

import org.springframework.stereotype.Component;
import overcloud.blog.entity.UserEntity;

@Component
public class UserResponseMapper {
    public UserResponse toUserResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .bio(userEntity.getBio())
                .image(userEntity.getImage())
                .build();
    }
}
