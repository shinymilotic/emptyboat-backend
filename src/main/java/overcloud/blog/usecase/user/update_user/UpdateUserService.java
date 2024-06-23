package overcloud.blog.usecase.user.update_user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.core.cache.RedisUtils;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ApiError;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.common.validation.ObjectsValidator;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.usecase.user.common.UserResponse;
import overcloud.blog.usecase.user.common.UserResponseMapper;

import java.util.Optional;

@Service
public class UpdateUserService {

    private final JpaUserRepository userRepository;
    private final SpringAuthenticationService authenticationService;
    private final UserResponseMapper userResponseMapper;
    private final ObjectsValidator<UpdateUserRequest> validator;
    private final RedisUtils redisUtils;
    private final ResFactory resFactory;

    public UpdateUserService(JpaUserRepository userRepository,
                             SpringAuthenticationService authenticationService,
                             UserResponseMapper userResponseMapper,
                             ObjectsValidator<UpdateUserRequest> validator,
                             RedisUtils redisUtils,
                             ResFactory resFactory) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.userResponseMapper = userResponseMapper;
        this.validator = validator;
        this.redisUtils = redisUtils;
        this.resFactory = resFactory;
    }

    @Transactional
    public RestResponse<UserResponse> updateUser(UpdateUserRequest updateUserDto) {
        Optional<ApiError> apiError = validator.validate(updateUserDto);

        if (apiError.isPresent()) {
            RestResponse<ApiError> res = resFactory.fail(UserResMsg.USER_UPDATE_FAILED, apiError.get());
            throw new InvalidDataException(res);
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
