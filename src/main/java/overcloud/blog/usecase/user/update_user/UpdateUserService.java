package overcloud.blog.usecase.user.update_user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.common.auth.service.SpringAuthenticationService;
import overcloud.blog.common.exceptionhandling.InvalidDataException;
import overcloud.blog.common.response.ApiError;
import overcloud.blog.common.response.ResFactory;
import overcloud.blog.common.response.RestResponse;
import overcloud.blog.common.validation.ObjectsValidator;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.usecase.user.common.UserResponse;
import overcloud.blog.usecase.user.common.UserResponseMapper;

import java.util.Optional;

@Service
public class UpdateUserService {

    private final IUserRepository userRepository;
    private final SpringAuthenticationService authenticationService;
    private final UserResponseMapper userResponseMapper;
    private final ObjectsValidator<UpdateUserRequest> validator;
    private final ResFactory resFactory;

    public UpdateUserService(IUserRepository userRepository,
                             SpringAuthenticationService authenticationService,
                             UserResponseMapper userResponseMapper,
                             ObjectsValidator<UpdateUserRequest> validator,
                             ResFactory resFactory) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.userResponseMapper = userResponseMapper;
        this.validator = validator;
        this.resFactory = resFactory;
    }

    @Transactional
    public RestResponse<UserResponse> updateUser(UpdateUserRequest updateUserDto) {
        Optional<ApiError> apiError = validator.validate(updateUserDto);

        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        String updateBio = updateUserDto.getBio();
        String updateImage = updateUserDto.getImage();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();

        currentUser.setBio(updateBio);
        currentUser.setImage(updateImage);
//        currentUser.setEmail(email);
        UserEntity updateUserEntity = userRepository.save(currentUser);
        return resFactory.success(UserResMsg.USER_UPDATE_SUCCESS, userResponseMapper.toUserResponse(updateUserEntity));
    }

}
