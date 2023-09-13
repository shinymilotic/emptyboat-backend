package overcloud.blog.application.user.update_user;

import co.elastic.clients.elasticsearch.core.UpdateRequest;
import org.springframework.stereotype.Service;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserError;
import overcloud.blog.application.user.core.UserResponse;
import overcloud.blog.application.user.core.UserResponseMapper;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.validation.ObjectsValidator;

import java.util.Optional;

@Service
public class UpdateUserService {

    private final UserRepository userRepository;

    private final SpringAuthenticationService authenticationService;

    private final UserResponseMapper userResponseMapper;

    private final ObjectsValidator<UpdateUserRequest> validator;


    public UpdateUserService(UserRepository userRepository,
                             SpringAuthenticationService authenticationService,
                             UserResponseMapper userResponseMapper,
                             ObjectsValidator<UpdateUserRequest> validator) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.userResponseMapper = userResponseMapper;
        this.validator = validator;
    }

    public UserResponse updateUser(UpdateUserRequest updateUserDto) {
        Optional<ApiError> apiError = validator.validate(updateUserDto);

        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

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

        return userResponseMapper.toUserResponse(updateUserEntity);
    }
}
