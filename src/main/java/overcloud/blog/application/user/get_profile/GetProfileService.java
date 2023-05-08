package overcloud.blog.application.user.get_profile;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.application.user.follow.core.utils.FollowUtils;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.util.Optional;

@Service
public class GetProfileService {

    private UserRepository userRepository;

    private SpringAuthenticationService authenticationService;

    private FollowUtils followUtils;

    public GetProfileService(UserRepository userRepository,
                           SpringAuthenticationService authenticationService,
                           FollowUtils followUtils) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.followUtils = followUtils;
    }

    public GetProfileResponse getProfile(String username) {
        GetProfileResponse profileResponse = new GetProfileResponse();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .orElseThrow(EntityNotFoundException::new)
                .orElseThrow(EntityNotFoundException::new);

        UserEntity user = userRepository.findByUsername(username);

        profileResponse.setUsername(user.getUsername());
        profileResponse.setFollowing(followUtils.isFollowing(Optional.of(currentUser), user));
        profileResponse.setBio(user.getBio());
        profileResponse.setImage(user.getImage());
        profileResponse.setFollowersCount(followUtils.getFollowingCount(user));

        return profileResponse;
    }
}
