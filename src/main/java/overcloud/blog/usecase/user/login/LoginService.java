package overcloud.blog.usecase.user.login;

import org.springframework.stereotype.Service;

import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.JwtUtils;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.validation.ObjectsValidator;
import overcloud.blog.usecase.user.core.*;
import overcloud.blog.usecase.user.refresh_token.RefreshTokenHash;
import overcloud.blog.usecase.user.refresh_token.RefreshTokenRepository;

import java.util.Optional;

@Service
public class LoginService {

    private final SpringAuthenticationService authenticationService;

    private final JwtUtils jwtUtils;

    private final ObjectsValidator<LoginRequest> validator;

    private final UserResponseMapper userResponseMapper;

    private final RefreshTokenRepository refreshTokenRepository;

    public LoginService(SpringAuthenticationService authenticationService,
                        JwtUtils jwtUtils,
                        ObjectsValidator<LoginRequest> validator,
                        UserResponseMapper userResponseMapper,
                        RefreshTokenRepository refreshTokenRepository) {
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
        this.validator = validator;
        this.userResponseMapper = userResponseMapper;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Optional<ApiError> apiError = validator.validate(loginRequest);

        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        String email = loginRequest.getEmail();
        String hashedPassword = loginRequest.getPassword();

        UserEntity user = authenticationService.authenticate(email, hashedPassword)
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_EMAIL_NO_EXIST)))
                .getUser();

        String accessToken = jwtUtils.encode(user.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());
        RefreshTokenHash refreshTokenHash = RefreshTokenHash.builder()
                .id(refreshToken)
                .email(email)
                .build();
        refreshTokenRepository.save(refreshTokenHash);
        return userResponseMapper.toAuthResponse(user,
                accessToken,
                refreshToken);
    }
}
