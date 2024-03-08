package overcloud.blog.usecase.auth.update_user;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.auth.bean.SecurityUser;
import overcloud.blog.infrastructure.auth.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.cache.RedisUtils;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;
import overcloud.blog.infrastructure.validation.ObjectsValidator;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.usecase.auth.common.UserError;
import overcloud.blog.usecase.auth.common.UserResponse;
import overcloud.blog.usecase.auth.common.UserResponseMapper;

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
