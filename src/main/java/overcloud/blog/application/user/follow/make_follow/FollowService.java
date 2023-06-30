package overcloud.blog.application.user.follow.make_follow;

import org.springframework.stereotype.Service;
import overcloud.blog.application.article.core.exception.InvalidDataException;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserError;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.application.user.follow.core.FollowEntity;
import overcloud.blog.application.user.follow.core.FollowId;
import overcloud.blog.application.user.follow.core.repository.FollowRepository;
import overcloud.blog.application.user.follow.core.utils.FollowUtils;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

@Service
public class FollowService {

    private final UserRepository userRepository;

    private final SpringAuthenticationService authenticationService;

    private final FollowRepository followRepository;

    private final FollowUtils followUtils;

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
        FollowEntity followEntity = new FollowEntity();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        UserEntity followee = userRepository.findByUsername(username);
        FollowId followId = new FollowId();
        followId.setFolloweeId(followee.getId());
        followId.setFollowerId(currentUser.getId());
        followEntity.setId(followId);
        followEntity.setFollower(currentUser);
        followEntity.setFollowee(followee);
        followRepository.save(followEntity);

        return toFollowResponse(followee);
    }

    public FollowResponse toFollowResponse(UserEntity followee) {
        return FollowResponse.builder()
                .username(followee.getUsername())
                .email(followee.getEmail())
                .bio(followee.getBio())
                .image(followee.getImage())
                .following(true)
                .followersCount(followUtils.getFollowingCount(followee))
                .build();
    }
}
