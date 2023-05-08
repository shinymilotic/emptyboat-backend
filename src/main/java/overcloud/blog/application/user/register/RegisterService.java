package overcloud.blog.application.user.register;

import org.springframework.stereotype.Service;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserResponse;
import overcloud.blog.application.user.core.exception.register.RegisterError;
import overcloud.blog.application.user.core.exception.register.RegisterInfoExistException;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.ApiValidationError;
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

    public RegisterResponse registerUser(RegisterRequest registrationDto) throws Exception {
        UserEntity userEntity = new UserEntity();
        String username = registrationDto.getUsername();
        String email = registrationDto.getEmail();
        String hashedPassword = authenticationService.encodePassword(registrationDto.getPassword());
        List<ApiValidationError> errors = new ArrayList<>();

        if(userRepository.findByUsername(username) != null) {
            ApiValidationError apiErrorDetail =
                    new ApiValidationError("registrationDto", "username"
                            ,username, RegisterError.USERNAME_EXIST.getValue());
            errors.add(apiErrorDetail);
        }

        if(userRepository.findByEmail(email) != null) {
            ApiValidationError apiErrorDetail =
                    new ApiValidationError("registrationDto", "email"
                            ,username,RegisterError.EMAIL_EXIST.getValue());
            errors.add(apiErrorDetail);
        }

        if(!errors.isEmpty()) {
            ApiError apiError = ApiError.from("Register information invalid");
            for (ApiValidationError error: errors) {
                apiError.getApiErrorDetails().add(error);
            }
            throw new RegisterInfoExistException(apiError);
        }

        userEntity.setUsername(registrationDto.getUsername());
        userEntity.setEmail(registrationDto.getEmail());
        userEntity.setPassword(hashedPassword);
        UserEntity user = userRepository.save(userEntity);

        RegisterResponse registerResponse = new RegisterResponse();
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setUsername(user.getUsername());
        userResponse.setToken(jwtUtils.encode(email));
        userResponse.setBio(user.getBio());
        userResponse.setImage(user.getImage());
        registerResponse.setUserResponse(userResponse);

        return registerResponse;
    }
}
