package overcloud.blog.usecase.auth.login;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.RefreshTokenEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.auth.bean.SecurityUser;
import overcloud.blog.infrastructure.auth.service.JwtUtils;
import overcloud.blog.infrastructure.auth.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.cache.RedisUtils;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;
import overcloud.blog.infrastructure.validation.ObjectsValidator;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;
import overcloud.blog.usecase.auth.common.AuthResponse;
import overcloud.blog.usecase.auth.common.UserError;
import overcloud.blog.usecase.auth.common.UserResponseMapper;

import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService {

    private final SpringAuthenticationService authenticationService;

    private final JwtUtils jwtUtils;

    private final ObjectsValidator<LoginRequest> validator;

    private final UserResponseMapper userResponseMapper;

    private final RedisUtils redisUtils;

    private final JpaRefreshTokenRepository refreshTokenRepository;


    public LoginService(SpringAuthenticationService authenticationService,
                        JwtUtils jwtUtils,
                        ObjectsValidator<LoginRequest> validator,
                        UserResponseMapper userResponseMapper,
                        RedisUtils redisUtils, JpaRefreshTokenRepository refreshTokenRepository) {
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
        this.validator = validator;
        this.userResponseMapper = userResponseMapper;
        this.redisUtils = redisUtils;
        this.refreshTokenRepository = refreshTokenRepository;
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

        saveDBRefreshToken(refreshToken, user.getId());
        cacheAuthUser(user);

        return userResponseMapper.toAuthResponse(user,
                accessToken,
                refreshToken);
    }

    private void saveDBRefreshToken(String refreshToken, UUID userId) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setUserId(userId);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    private void cacheAuthUser(UserEntity user) {
        SecurityUser securityUser = new SecurityUser(user);
        redisUtils.set(user.getEmail(), new UsernamePasswordAuthenticationToken(securityUser, securityUser.getPassword(), securityUser.getAuthorities()));
    }
}
