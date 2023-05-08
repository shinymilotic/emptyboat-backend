package overcloud.blog.application.user.follow.make_follow;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.application.user.follow.core.FollowEntity;
import overcloud.blog.application.user.follow.core.FollowId;
import overcloud.blog.application.user.follow.core.repository.FollowRepository;
import overcloud.blog.application.user.follow.core.utils.FollowUtils;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

@Service
public class FollowService {

    private UserRepository userRepository;

    private SpringAuthenticationService authenticationService;

    private FollowRepository followRepository;

    private FollowUtils followUtils;

    public FollowService(UserRepository userRepository,
                           SpringAuthenticationService authenticationService,
                           FollowRepository followRepository,
                           FollowUtils followUtils) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.followRepository = followRepository;
        this.followUtils = followUtils;
    }

    public FollowResponse followUser(String username) {
        FollowResponse followUserResponse = new FollowResponse();
        FollowEntity followEntity = new FollowEntity();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .orElseThrow(EntityNotFoundException::new)
                .orElseThrow(EntityNotFoundException::new);

        UserEntity followee = userRepository.findByUsername(username);
        FollowId followId = new FollowId();
        followId.setFolloweeId(followee.getId());
        followId.setFollowerId(currentUser.getId());
        followEntity.setId(followId);
        followEntity.setFollower(currentUser);
        followEntity.setFollowee(followee);
        followRepository.save(followEntity);

        followUserResponse.setUsername(followee.getUsername());
        followUserResponse.setBio(followee.getBio());
        followUserResponse.setImage(followee.getImage());
        followUserResponse.setFollowing(true);
        followUserResponse.setFollowersCount(followUtils.getFollowingCount(followee));

        return followUserResponse;
    }
}
