package overcloud.blog.usecase.user.get_profile;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.auth.bean.SecurityUser;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.utils.validation.ObjectsValidator;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetProfileService {
    private final IUserRepository userRepository;
    private final SpringAuthenticationService authenticationService;
    private final ObjectsValidator validator;

    public GetProfileService(IUserRepository userRepository,
                             SpringAuthenticationService authenticationService,
                             ObjectsValidator validator) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.validator = validator;
    }

    @Transactional(readOnly = true)
    public GetProfileResponse getProfile(String username) {
        Optional<SecurityUser> currentSecurityUser = authenticationService.getCurrentUser();
        UserEntity currentUser = null;
        if (currentSecurityUser.isPresent()) {
            currentUser = currentSecurityUser.get().getUser();
        }

        UUID currentUserId = null;
        if (currentUser != null) {
            currentUserId = currentUser.getUserId();
        }

        return userRepository.findProfile(username, currentUserId);
    }

}
