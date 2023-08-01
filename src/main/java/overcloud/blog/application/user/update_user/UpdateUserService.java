package overcloud.blog.application.user.update_user;

import org.springframework.stereotype.Service;
import overcloud.blog.application.article.core.exception.InvalidDataException;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserError;
import overcloud.blog.application.user.core.UserResponse;
import overcloud.blog.application.user.core.UserResponseMapper;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

@Service
public class UpdateUserService {

    private final UserRepository userRepository;

    private final SpringAuthenticationService authenticationService;

    private final UserResponseMapper userResponseMapper;

    public UpdateUserService(UserRepository userRepository,
                           SpringAuthenticationService authenticationService,
                             UserResponseMapper userResponseMapper) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.userResponseMapper = userResponseMapper;
    }

    public UserResponse updateUser(UpdateUserRequest updateUserDto) {
        String email = updateUserDto.getEmail();
        String updateBio = updateUserDto.getBio();
        String updateImage = updateUserDto.getImage();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        currentUser.setBio(updateBio);
        currentUser.setImage(updateImage);
        currentUser.setEmail(email);
        UserEntity updateUserEntity = userRepository.save(currentUser);

        return userResponseMapper.toUserResponse(updateUserEntity, "");
    }
}
