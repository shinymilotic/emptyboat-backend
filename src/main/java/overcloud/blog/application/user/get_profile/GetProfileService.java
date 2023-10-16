package overcloud.blog.application.user.get_profile;

import org.springframework.stereotype.Service;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserError;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.application.user.follow.core.utils.FollowUtils;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.util.Optional;

@Service
public class GetProfileService {

    private final UserRepository userRepository;

    private final SpringAuthenticationService authenticationService;

    private final FollowUtils followUtils;

    public GetProfileService(UserRepository userRepository,
                           SpringAuthenticationService authenticationService,
                           FollowUtils followUtils) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.followUtils = followUtils;
    }

    public GetProfileResponse getProfile(String username) {
        GetProfileResponse profileResponse = new GetProfileResponse();
        Optional<SecurityUser> currentSecurityUser = authenticationService.getCurrentUser();
        UserEntity currentUser = null;
        if(currentSecurityUser.isPresent()) {
            currentUser = currentSecurityUser.get().getUser();
        }

        UserEntity user = userRepository.findByUsername(username);

        profileResponse.setUsername(user.getUsername());
        profileResponse.setEmail(user.getEmail());
        profileResponse.setFollowing(followUtils.isFollowing(currentUser, user));
        profileResponse.setBio(user.getBio());
        profileResponse.setImage(user.getImage());
        profileResponse.setFollowersCount(followUtils.getFollowingCount(user));

        return profileResponse;
    }
}
