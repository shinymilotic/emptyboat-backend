package overcloud.blog.application.user.login;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import overcloud.blog.application.article.core.exception.InvalidDataException;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserError;
import overcloud.blog.application.user.core.UserResponse;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.JwtUtils;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.validation.ObjectsValidator;

import java.util.Optional;

@Service
public class LoginService {

    private final SpringAuthenticationService authenticationService;

    private final JwtUtils jwtUtils;

    private final ObjectsValidator<LoginRequest> validator;

    public LoginService(SpringAuthenticationService authenticationService,
                        JwtUtils jwtUtils,
                        ObjectsValidator<LoginRequest> validator) {
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
        this.validator = validator;
    }

    public UserResponse login(LoginRequest loginRequest) {
        Optional<ApiError> apiError = validator.validate(loginRequest);

        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        String email = loginRequest.getEmail();
        String hashedPassword = loginRequest.getPassword();
        UserEntity user = authenticationService.authenticate(email, hashedPassword)
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

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
