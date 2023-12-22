package overcloud.blog.usecase.user.register;

import org.springframework.stereotype.Service;

import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.ApiErrorDetail;
import overcloud.blog.infrastructure.security.service.JwtUtils;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.validation.ObjectsValidator;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.usecase.user.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RegisterService {

    private final JpaUserRepository userRepository;

    private final SpringAuthenticationService authenticationService;

    private final JwtUtils jwtUtils;

    private final ObjectsValidator<RegisterRequest> validator;

    private final UserResponseMapper userResponseMapper;

    public RegisterService(JpaUserRepository userRepository,
                           SpringAuthenticationService authenticationService,
                           JwtUtils jwtUtils,
                           ObjectsValidator<RegisterRequest> validator,
                           UserResponseMapper userResponseMapper) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
        this.validator = validator;
        this.userResponseMapper = userResponseMapper;
    }

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

        return userResponseMapper.toAuthResponse(savedUser,
                jwtUtils.encode(savedUser.getEmail()),
                jwtUtils.generateRefreshToken(savedUser.getEmail()));
    }
}
