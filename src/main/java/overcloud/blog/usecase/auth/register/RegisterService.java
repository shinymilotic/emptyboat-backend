package overcloud.blog.usecase.auth.register;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.RefreshTokenEntity;
import overcloud.blog.entity.RoleEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.auth.AuthError;
import overcloud.blog.infrastructure.auth.service.JwtUtils;
import overcloud.blog.infrastructure.auth.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;
import overcloud.blog.infrastructure.validation.Error;
import overcloud.blog.infrastructure.validation.ObjectsValidator;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;
import overcloud.blog.repository.jparepository.JpaRoleRepository;
import overcloud.blog.usecase.auth.common.UserError;
import overcloud.blog.usecase.auth.common.UserResponse;
import overcloud.blog.usecase.auth.common.UserResponseMapper;
import overcloud.blog.usecase.email.EmailService;

import java.util.*;

@Service
public class RegisterService {

    private final IUserRepository userRepository;

    private final JpaRoleRepository roleRepository;

    private final SpringAuthenticationService authenticationService;

    private final JwtUtils jwtUtils;

    private final ObjectsValidator<RegisterRequest> validator;

    private final UserResponseMapper userResponseMapper;

    private final EmailService emailService;

    private final JpaRefreshTokenRepository refreshTokenRepository;

    public RegisterService(IUserRepository userRepository, JpaRoleRepository roleRepository,
                           SpringAuthenticationService authenticationService,
                           JwtUtils jwtUtils,
                           ObjectsValidator<RegisterRequest> validator,
                           UserResponseMapper userResponseMapper, EmailService emailService,
                           JpaRefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
        this.validator = validator;
        this.userResponseMapper = userResponseMapper;
        this.emailService = emailService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public UserResponse registerUser(RegisterRequest registrationDto, HttpServletResponse response) {
        Optional<ApiError> apiError = validator.validate(registrationDto);
        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        String username = registrationDto.getUsername();
        String email = registrationDto.getEmail();
        String hashedPassword = authenticationService.encodePassword(registrationDto.getPassword());
        List<Error> errors = new ArrayList<>();

        if (userRepository.findByUsername(username) != null) {
            errors.add(UserError.USER_USERNAME_EXIST);
        }

        if (userRepository.findByEmail(email) != null) {
            errors.add(UserError.USER_EMAIL_EXIST);
        }

        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }

        UserEntity userEntity = UserEntity.builder()
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .image("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png")
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

        String refreshToken = jwtUtils.generateRefreshToken(savedUser.getEmail());
        saveDBRefreshToken(refreshToken, savedUser.getId());

        emailService.sendSimpleMessage(savedUser.getEmail(), "Registration email confirm!",
                "Please click on the confirmation link: http://localhost:4200/confirmEmail/" + refreshToken);

        return userResponseMapper.toUserResponse(savedUser);
    }

    private void saveDBRefreshToken(String refreshToken, UUID userId) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setUserId(userId);
        refreshTokenRepository.save(refreshTokenEntity);
    }

}
