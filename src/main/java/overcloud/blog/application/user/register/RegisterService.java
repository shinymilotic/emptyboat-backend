package overcloud.blog.application.user.register;

import org.springframework.stereotype.Service;
import overcloud.blog.application.article.core.exception.InvalidDataException;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserError;
import overcloud.blog.application.user.core.UserResponse;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.application.user.update_user.UpdateUserRequest;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.ApiErrorDetail;
import overcloud.blog.infrastructure.security.service.JwtUtils;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.validation.ObjectsValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RegisterService {

    private final UserRepository userRepository;

    private final SpringAuthenticationService authenticationService;

    private final JwtUtils jwtUtils;

    private final ObjectsValidator<RegisterRequest> validator;

    public RegisterService(UserRepository userRepository,
                           SpringAuthenticationService authenticationService,
                           JwtUtils jwtUtils,
                           ObjectsValidator<RegisterRequest> validator) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
        this.validator = validator;
    }

    public UserResponse registerUser(RegisterRequest registrationDto) {
        Optional<ApiError> apiError = validator.validate(registrationDto);

        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        String username = registrationDto.getUsername();
        String email = registrationDto.getEmail();
        String hashedPassword = authenticationService.encodePassword(registrationDto.getPassword());
        List<ApiErrorDetail> errors = new ArrayList<>();

        if(userRepository.findByUsername(username) != null) {
            errors.add(ApiErrorDetail.from(UserError.USER_USERNAME_EXIST));
        }

        if(userRepository.findByEmail(email) != null) {
            errors.add(ApiErrorDetail.from(UserError.USER_EMAIL_EXIST));
        }

        if(!errors.isEmpty()) {
            throw new InvalidDataException(ApiError.from(errors));
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registrationDto.getUsername());
        userEntity.setEmail(registrationDto.getEmail());
        userEntity.setPassword(hashedPassword);
        UserEntity user = userRepository.save(userEntity);

        return toUserResponse(user);
    }

    public UserResponse toUserResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .bio(userEntity.getBio())
                .token(jwtUtils.encode(userEntity.getEmail()))
                .image(userEntity.getImage())
                .build();
    }
}
