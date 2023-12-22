package overcloud.blog.usecase.user.update_user;

import org.springframework.stereotype.Service;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.validation.ObjectsValidator;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.usecase.user.core.UserError;
import overcloud.blog.usecase.user.core.UserResponse;
import overcloud.blog.usecase.user.core.UserResponseMapper;

import java.util.Optional;

@Service
public class UpdateUserService {

    private final JpaUserRepository userRepository;

    private final SpringAuthenticationService authenticationService;

    private final UserResponseMapper userResponseMapper;

    private final ObjectsValidator<UpdateUserRequest> validator;


    public UpdateUserService(JpaUserRepository userRepository,
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
