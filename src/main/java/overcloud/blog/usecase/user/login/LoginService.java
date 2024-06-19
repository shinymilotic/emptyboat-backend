package overcloud.blog.usecase.user.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.hibernate.mapping.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.core.cache.RedisUtils;
import overcloud.blog.entity.RefreshTokenEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;
import overcloud.blog.usecase.common.auth.service.JwtUtils;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.exceptionhandling.ApiError;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ApiValidationError;
import overcloud.blog.usecase.common.response.ExceptionFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.common.validation.ObjectsValidator;
import overcloud.blog.usecase.user.common.UserError;
import overcloud.blog.usecase.user.common.UserResponse;
import overcloud.blog.usecase.user.common.UserResponseMapper;

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
    private final ExceptionFactory exceptionFactory;

    public LoginService(SpringAuthenticationService authenticationService,
                        JwtUtils jwtUtils,
                        ObjectsValidator<LoginRequest> validator,
                        UserResponseMapper userResponseMapper,
                        RedisUtils redisUtils,
                        JpaRefreshTokenRepository refreshTokenRepository,
                        ExceptionFactory exceptionFactory) {
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
        this.validator = validator;
        this.userResponseMapper = userResponseMapper;
        this.redisUtils = redisUtils;
        this.refreshTokenRepository = refreshTokenRepository;
        this.exceptionFactory = exceptionFactory;
    }

    @Transactional
    public RestResponse<UserResponse> login(LoginRequest loginRequest, HttpServletResponse response) {
        List<ApiValidationError> apiError = validator.validate(loginRequest);
        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        String email = loginRequest.getEmail();
        String hashedPassword = loginRequest.getPassword();
        UserEntity user = authenticationService.authenticate(email, hashedPassword)
                .orElseThrow(() -> new InvalidDataException(UserError.USER_EMAIL_NO_EXIST))
                .getUser();

        if (!user.isEnable()) {
            throw new InvalidDataException(UserError.USER_NON_ENABLED);
        }

        String accessToken = jwtUtils.encode(user.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());

        saveDBRefreshToken(refreshToken, user.getId());

        Cookie jwtTokenCookie = new Cookie("jwtToken", accessToken);
        jwtTokenCookie.setMaxAge(86400);
        jwtTokenCookie.setSecure(false);
        jwtTokenCookie.setHttpOnly(true);
        jwtTokenCookie.setPath("/");
        jwtTokenCookie.setDomain("localhost");
//      jwtTokenCookie.setSecure(true);
        Cookie jwtRefreshTokenCookie = new Cookie("refreshToken", refreshToken);
        jwtRefreshTokenCookie.setMaxAge(86400);
        jwtRefreshTokenCookie.setSecure(false);
        jwtRefreshTokenCookie.setHttpOnly(true);
        jwtRefreshTokenCookie.setPath("/");
        jwtRefreshTokenCookie.setDomain("localhost");
//        jwtRefreshTokenCookie.setSecure(true);
//        jwtRefreshTokenCookie.setAttribute("SameSite", "None");

        response.addCookie(jwtTokenCookie);
        response.addCookie(jwtRefreshTokenCookie);

        return userResponseMapper.toUserResponse(user);
    }

    private void saveDBRefreshToken(String refreshToken, UUID userId) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setUserId(userId);
        refreshTokenRepository.save(refreshTokenEntity);
    }
}
