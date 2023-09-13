package overcloud.blog.application.user.core;

import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper {
    public UserResponse toUserResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .bio(userEntity.getBio())
                .image(userEntity.getImage())
                .build();
    }

    public AuthResponse toAuthResponse(UserEntity user, String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .image(user.getImage())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
