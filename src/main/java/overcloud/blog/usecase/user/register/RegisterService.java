package overcloud.blog.usecase.user.register;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.RefreshTokenEntity;
import overcloud.blog.entity.RoleEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IRoleRepository;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;
import overcloud.blog.usecase.common.auth.service.JwtUtils;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ApiError;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.common.validation.ObjectsValidator;
import overcloud.blog.usecase.email.EmailService;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.usecase.user.common.UserResponse;
import overcloud.blog.usecase.user.common.UserResponseMapper;
import java.util.*;

@Service
public class RegisterService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final SpringAuthenticationService authenticationService;
    private final JwtUtils jwtUtils;
    private final ObjectsValidator<RegisterRequest> validator;
    private final UserResponseMapper userResponseMapper;
    private final EmailService emailService;
    private final JpaRefreshTokenRepository refreshTokenRepository;
    private final ResFactory resFactory;

    public RegisterService(IUserRepository userRepository, IRoleRepository roleRepository,
                           SpringAuthenticationService authenticationService,
                           JwtUtils jwtUtils,
                           ObjectsValidator<RegisterRequest> validator,
                           UserResponseMapper userResponseMapper, EmailService emailService,
                           JpaRefreshTokenRepository refreshTokenRepository,
                           ResFactory resFactory) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
        this.validator = validator;
        this.userResponseMapper = userResponseMapper;
        this.emailService = emailService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.resFactory = resFactory;
    }

    @Transactional
    public RestResponse<Void> registerUser(RegisterRequest registrationDto, HttpServletResponse response) {
        Optional<ApiError> apiError = validator.validate(registrationDto);
        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        String username = registrationDto.getUsername();
        String email = registrationDto.getEmail();
        String hashedPassword = authenticationService.encodePassword(registrationDto.getPassword());

        if (userRepository.findByUsername(username) != null) {
            throw new InvalidDataException(resFactory.fail(UserResMsg.USER_USERNAME_EXIST));
        }

        if (userRepository.findByEmail(email) != null) {
            throw new InvalidDataException(resFactory.fail(UserResMsg.USER_EMAIL_EXIST));
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
            throw new InvalidDataException(resFactory.fail(UserResMsg.AUTHORIZE_FAILED));
        }

        String refreshToken = jwtUtils.generateRefreshToken(savedUser.getEmail());
        saveDBRefreshToken(refreshToken, savedUser.getId());

        emailService.sendSimpleMessage(savedUser.getEmail(), "Registration email confirm!",
                "Please click on the confirmation link: http://localhost:4200/confirmEmail/" + refreshToken);

        return resFactory.success(UserResMsg.USER_REGISTER_SUCCESS, null);
    }

    private void saveDBRefreshToken(String refreshToken, UUID userId) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setUserId(userId);
        refreshTokenRepository.save(refreshTokenEntity);
    }

}
