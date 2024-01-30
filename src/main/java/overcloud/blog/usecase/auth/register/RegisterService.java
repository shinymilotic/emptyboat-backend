package overcloud.blog.usecase.auth.register;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import overcloud.blog.entity.RefreshTokenEntity;
import overcloud.blog.entity.RoleEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.entity.UserRole;
import overcloud.blog.infrastructure.AuthError;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.infrastructure.cache.RedisUtils;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.ApiErrorDetail;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.JwtUtils;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.validation.ObjectsValidator;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.repository.IUserRoleRepository;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;
import overcloud.blog.repository.jparepository.JpaRoleRepository;
import overcloud.blog.usecase.auth.common.AuthResponse;
import overcloud.blog.usecase.auth.common.UserError;
import overcloud.blog.usecase.auth.common.UserResponseMapper;

import java.util.*;

@Service
public class RegisterService {

    private final IUserRepository userRepository;

    private final JpaRoleRepository roleRepository;

    private final SpringAuthenticationService authenticationService;

    private final JwtUtils jwtUtils;

    private final ObjectsValidator<RegisterRequest> validator;

    private final UserResponseMapper userResponseMapper;

    private final RedisUtils redisUtils;

    private final JpaRefreshTokenRepository refreshTokenRepository;

    public RegisterService(IUserRepository userRepository, JpaRoleRepository roleRepository,
                           SpringAuthenticationService authenticationService,
                           JwtUtils jwtUtils,
                           ObjectsValidator<RegisterRequest> validator,
                           UserResponseMapper userResponseMapper, RedisUtils redisUtils,
                           JpaRefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
        this.validator = validator;
        this.userResponseMapper = userResponseMapper;
        this.redisUtils = redisUtils;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public AuthResponse registerUser(RegisterRequest registrationDto) {
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

        UserEntity userEntity = UserEntity.builder()
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .password(hashedPassword)
                .build();

        UserEntity savedUser = userRepository.save(userEntity);
        Optional<RoleEntity> role = roleRepository.findById(UUID.fromString("80e1e7af-0f80-4a5f-ab42-bfbfa6513da9"));

        if (role.isPresent()) {
            Set<RoleEntity> roleEntitySet = new HashSet<>();
            roleEntitySet.add(role.get());
            savedUser.setRoles(roleEntitySet);
        } else {
            throw new InvalidDataException(AuthError.AUTHORIZE_FAILED);
        }

        String accessToken = jwtUtils.encode(savedUser.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(savedUser.getEmail());
        saveDBRefreshToken(refreshToken, savedUser.getId());
        cacheAuthUser(savedUser);

        return userResponseMapper.toAuthResponse(savedUser,
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
