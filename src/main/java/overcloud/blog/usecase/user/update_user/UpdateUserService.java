package overcloud.blog.usecase.user.update_user;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.core.cache.RedisUtils;
import overcloud.blog.core.validation.ObjectsValidator;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.usecase.common.auth.bean.SecurityUser;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.exceptionhandling.ApiError;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.user.common.UserError;
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

    public UpdateUserService(JpaUserRepository userRepository,
                             SpringAuthenticationService authenticationService,
                             UserResponseMapper userResponseMapper,
                             ObjectsValidator<UpdateUserRequest> validator,
                             RedisUtils redisUtils) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.userResponseMapper = userResponseMapper;
        this.validator = validator;
        this.redisUtils = redisUtils;
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
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        currentUser.setBio(updateBio);
        currentUser.setImage(updateImage);
//        currentUser.setEmail(email);
        UserEntity updateUserEntity = userRepository.save(currentUser);
        return userResponseMapper.toUserResponse(updateUserEntity);
    }

}
