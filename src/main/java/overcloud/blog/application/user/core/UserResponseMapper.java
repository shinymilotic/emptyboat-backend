package overcloud.blog.application.user.core;

import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper {
    public UserResponse toUserResponse(UserEntity userEntity, String token) {
        return UserResponse.builder()
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .bio(userEntity.getBio())
                .token(token)
                .image(userEntity.getImage())
                .build();
    }

}
