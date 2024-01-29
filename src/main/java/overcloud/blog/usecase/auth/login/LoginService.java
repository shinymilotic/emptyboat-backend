package overcloud.blog.usecase.auth.login;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.infrastructure.cache.RedisUtils;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.JwtUtils;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.validation.ObjectsValidator;
import overcloud.blog.usecase.auth.common.AuthResponse;
import overcloud.blog.usecase.auth.common.UserError;
import overcloud.blog.usecase.auth.common.UserResponseMapper;
import overcloud.blog.usecase.auth.refresh_token.RefreshTokenRepository;

import java.util.Optional;

@Service
public class LoginService {

    private final SpringAuthenticationService authenticationService;

    private final JwtUtils jwtUtils;

    private final ObjectsValidator<LoginRequest> validator;

    private final UserResponseMapper userResponseMapper;

    private RedisUtils redisUtils;

    private final RefreshTokenRepository refreshTokenRepository;

    public LoginService(SpringAuthenticationService authenticationService,
                        JwtUtils jwtUtils,
                        ObjectsValidator<LoginRequest> validator,
                        UserResponseMapper userResponseMapper,
                        RefreshTokenRepository refreshTokenRepository,
                        RedisUtils redisUtils) {
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
        this.validator = validator;
        this.userResponseMapper = userResponseMapper;
        this.refreshTokenRepository = refreshTokenRepository;
        this.redisUtils = redisUtils;
    }

    @Transactional
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

        SecurityUser securityUser = new SecurityUser(user);
        redisUtils.set(email, new UsernamePasswordAuthenticationToken(securityUser, securityUser.getPassword(), securityUser.getAuthorities()));

        return userResponseMapper.toAuthResponse(user,
                accessToken,
                refreshToken);
    }
}
