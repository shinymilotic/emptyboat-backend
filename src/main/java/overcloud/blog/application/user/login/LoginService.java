package overcloud.blog.application.user.login;

import org.springframework.stereotype.Service;
import overcloud.blog.application.user.core.*;
import overcloud.blog.infrastructure.InvalidDataException;
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

    private final UserResponseMapper userResponseMapper;

    public LoginService(SpringAuthenticationService authenticationService,
                        JwtUtils jwtUtils,
                        ObjectsValidator<LoginRequest> validator,
                        UserResponseMapper userResponseMapper) {
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
        this.validator = validator;
        this.userResponseMapper = userResponseMapper;
    }

    public UserResponse login(LoginRequest loginRequest) {
        Optional<ApiError> apiError = validator.validate(loginRequest);

        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        String email = loginRequest.getEmail();
        String hashedPassword = loginRequest.getPassword();
        UserEntity user = authenticationService.authenticate(email, hashedPassword)
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_EMAIL_NO_EXIST)))
                .getUser();

        return userResponseMapper.toUserResponse(user,
                jwtUtils.encode(user.getEmail()),
                jwtUtils.generateRefreshToken(user.getEmail()));
    }
}
