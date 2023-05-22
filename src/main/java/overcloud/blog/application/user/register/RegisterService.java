package overcloud.blog.application.user.register;

import org.springframework.stereotype.Service;
import overcloud.blog.application.article.core.exception.InvalidDataException;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserResponse;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.ApiErrorDetail;
import overcloud.blog.infrastructure.security.service.JwtUtils;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegisterService {

    private UserRepository userRepository;

    private SpringAuthenticationService authenticationService;

    private JwtUtils jwtUtils;


    public RegisterService(UserRepository userRepository,
                           SpringAuthenticationService authenticationService,
                           JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
    }

    public UserResponse registerUser(RegisterRequest registrationDto) {
        UserEntity userEntity = new UserEntity();
        String username = registrationDto.getUsername();
        String email = registrationDto.getEmail();
        String hashedPassword = authenticationService.encodePassword(registrationDto.getPassword());
        List<ApiErrorDetail> errors = new ArrayList<>();

        if(userRepository.findByUsername(username) != null) {
            ApiErrorDetail apiErrorDetail =
                    new ApiErrorDetail("registrationDto","Username already used");
            errors.add(apiErrorDetail);
        }

        if(userRepository.findByEmail(email) != null) {
            ApiErrorDetail apiErrorDetail =
                    new ApiErrorDetail("registrationDto", "Email already used");
            errors.add(apiErrorDetail);
        }

        if(!errors.isEmpty()) {
            throw new InvalidDataException(ApiError.from(errors));
        }

        userEntity.setUsername(registrationDto.getUsername());
        userEntity.setEmail(registrationDto.getEmail());
        userEntity.setPassword(hashedPassword);
        UserEntity user = userRepository.save(userEntity);

        return toUserResponse(user);
    }

    public UserResponse toUserResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .bio(userEntity.getBio())
                .token(jwtUtils.encode(userEntity.getEmail()))
                .image(userEntity.getImage())
                .build();
    }
}
