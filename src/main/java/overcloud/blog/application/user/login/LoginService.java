package overcloud.blog.application.user.login;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserResponse;
import overcloud.blog.application.user.core.exception.login.LoginError;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.JwtUtils;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

@Service
public class LoginService {

    private SpringAuthenticationService authenticationService;

    private JwtUtils jwtUtils;

    public LoginService(SpringAuthenticationService authenticationService, JwtUtils jwtUtils) {
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String hashedPassword = loginRequest.getPassword();
        SecurityUser securityUser = authenticationService.authenticate(email, hashedPassword)
                // build exception
                .orElseThrow(() -> new EntityNotFoundException(LoginError.LOGIN_INFO_INVALID.getValue()));
        UserEntity user = securityUser.getUser().
                orElseThrow(EntityNotFoundException::new);
        String token = jwtUtils.encode(email);

        LoginResponse loginResponse = new LoginResponse();
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(securityUser.getUsername());
        userResponse.setToken(token);
        userResponse.setImage(user.getImage());
        userResponse.setBio(user.getBio());
        userResponse.setEmail(user.getEmail());
        loginResponse.setUserResponse(userResponse);

        return loginResponse;
    }
}
