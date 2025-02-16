package overcloud.blog.usecase.user.register;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.f4b6a3.uuid.UuidCreator;
import overcloud.blog.auth.service.JwtUtils;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ApiError;
import overcloud.blog.response.ResFactory;
import overcloud.blog.utils.validation.ObjectsValidator;
import overcloud.blog.entity.RefreshTokenEntity;
import overcloud.blog.entity.RoleEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IRoleRepository;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;
import overcloud.blog.usecase.email.EmailService;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.util.*;

@Service
public class RegisterService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final SpringAuthenticationService authenticationService;
    private final JwtUtils jwtUtils;
    private final ObjectsValidator<RegisterRequest> validator;
    private final EmailService emailService;
    private final JpaRefreshTokenRepository refreshTokenRepository;
    private final ResFactory resFactory;

    public RegisterService(IUserRepository userRepository, IRoleRepository roleRepository,
                           SpringAuthenticationService authenticationService,
                           JwtUtils jwtUtils,
                           ObjectsValidator<RegisterRequest> validator,
                           EmailService emailService,
                           JpaRefreshTokenRepository refreshTokenRepository,
                           ResFactory resFactory) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
        this.validator = validator;
        this.emailService = emailService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.resFactory = resFactory;
    }

    @Transactional
    public Void registerUser(RegisterRequest registrationDto, HttpServletResponse response) {
        Optional<ApiError> apiError = validator.validate(registrationDto);
        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        String username = registrationDto.getUsername();
        String email = registrationDto.getEmail();
        String hashedPassword = authenticationService.encodePassword(registrationDto.getPassword());

        if (userRepository.findByUsername(username) != null) {
            throw resFactory.fail(UserResMsg.USER_USERNAME_EXIST);
        }

        if (userRepository.findByEmail(email) != null) {
            throw resFactory.fail(UserResMsg.USER_EMAIL_EXIST);
        }

        UserEntity userEntity = UserEntity.builder()
                .userId(UuidCreator.getTimeOrderedEpoch())
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .image("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png")
                .password(hashedPassword)
                .enable(true)
                .build();

        UserEntity savedUser = userRepository.save(userEntity);
        Optional<RoleEntity> role = roleRepository.findById(UUID.fromString("80e1e7af-0f80-4a5f-ab42-bfbfa6513da9"));

        if (role.isPresent()) {
            Set<RoleEntity> roleEntitySet = new HashSet<>();
            roleEntitySet.add(role.get());
            savedUser.setRoles(roleEntitySet);
        } else {
            throw resFactory.fail(UserResMsg.AUTHORIZE_FAILED);
        }

        String refreshToken = jwtUtils.generateRefreshToken(savedUser.getEmail());
        saveDBRefreshToken(refreshToken, savedUser.getUserId());

        // emailService.sendSimpleMessage(savedUser.getEmail(), "Registration email confirm!",
        //         "Please click on the confirmation link: http://localhost:4200/confirmEmail/" + refreshToken);

        return null;
    }

    private void saveDBRefreshToken(String refreshToken, UUID userId) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setRefreshTokenId(UuidCreator.getTimeOrderedEpoch());
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setUserId(userId);
        refreshTokenRepository.save(refreshTokenEntity);
    }

}
