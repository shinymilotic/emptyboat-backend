package overcloud.blog.application.user.core;

import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper {
    public UserResponse toUserResponse(UserEntity userEntity, String accessToken, String refreshToken) {
        return UserResponse.builder()
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .bio(userEntity.getBio())
                .image(userEntity.getImage())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
