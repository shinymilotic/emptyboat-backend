package overcloud.blog.usecase.user.update_user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ApiError;
import overcloud.blog.utils.validation.ObjectsValidator;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.UserRepository;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.usecase.user.common.UserResponse;
import overcloud.blog.usecase.user.common.UserResponseMapper;

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

    @Transactional
    public UserResponse updateUser(UpdateUserRequest updateUserDto) {
        Optional<ApiError> apiError = validator.validate(updateUserDto);

        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        String updateBio = updateUserDto.getBio();
        String updateImage = updateUserDto.getImage();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> validator.fail(UserResMsg.USER_NOT_FOUND))
                .getUser();

        currentUser.setBio(updateBio);
        currentUser.setImage(updateImage);
//        currentUser.setEmail(email);
        UserEntity updateUserEntity = userRepository.save(currentUser);
        return userResponseMapper.toUserResponse(updateUserEntity);
    }

}
