package overcloud.blog.application.user.get_current_user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserResponse;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.JwtUtils;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

@Service
public class GetCurrentUserService {

    private SpringAuthenticationService authenticationService;

    private JwtUtils jwtUtils;

    public GetCurrentUserService(SpringAuthenticationService authenticationService, JwtUtils jwtUtils) {
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
    }

    public CurrentUserResponse getCurrentUser() {
        UserEntity currentUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .orElseThrow(EntityNotFoundException::new)
                .orElseThrow(EntityNotFoundException::new);
        CurrentUserResponse currentUserResponse = new CurrentUserResponse();
        UserResponse userResponse = new UserResponse();

        userResponse.setUsername(currentUser.getUsername());
        userResponse.setEmail(currentUser.getEmail());
        userResponse.setBio(currentUser.getBio());
        userResponse.setToken(jwtUtils.encode(currentUser.getEmail()));
        userResponse.setImage(currentUser.getImage());
        currentUserResponse.setUserResponse(userResponse);

        return currentUserResponse;
    }
}
