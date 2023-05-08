package overcloud.blog.application.user.follow.make_unfollow;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.application.user.follow.core.FollowEntity;
import overcloud.blog.application.user.follow.core.repository.FollowRepository;
import overcloud.blog.application.user.follow.core.utils.FollowUtils;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.util.List;

@Service
public class UnfollowService {

    private UserRepository userRepository;

    private SpringAuthenticationService authenticationService;

    private FollowRepository followRepository;

    private FollowUtils followUtils;

    public UnfollowService(UserRepository userRepository,
                           SpringAuthenticationService authenticationService,
                           FollowRepository followRepository,
                           FollowUtils followUtils) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.followRepository = followRepository;
        this.followUtils = followUtils;
    }

    public UnfollowResponse unfollowUser(String username) {
        UnfollowResponse unfollowResponse = new UnfollowResponse();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .orElseThrow(EntityNotFoundException::new)
                .orElseThrow(EntityNotFoundException::new);

        UserEntity followee = userRepository.findByUsername(username);

        List<FollowEntity> followEntity = followRepository.getFollowing(currentUser.getUsername(), followee.getUsername());
        followRepository.delete(followEntity.get(0));

        unfollowResponse.setUsername(followee.getUsername());
        unfollowResponse.setBio(followee.getBio());
        unfollowResponse.setImage(followee.getImage());
        unfollowResponse.setFollowing(false);
        unfollowResponse.setFollowersCount(followUtils.getFollowingCount(followee));

        return unfollowResponse;
    }
}
