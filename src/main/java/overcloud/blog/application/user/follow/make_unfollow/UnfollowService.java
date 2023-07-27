package overcloud.blog.application.user.follow.make_unfollow;

import org.springframework.stereotype.Service;
import overcloud.blog.application.article.core.exception.InvalidDataException;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserError;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.application.user.follow.core.FollowEntity;
import overcloud.blog.application.user.follow.core.repository.FollowRepository;
import overcloud.blog.application.user.follow.core.utils.FollowUtils;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
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
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        UserEntity followee = userRepository.findByUsername(username);

        List<FollowEntity> followEntity = followRepository.getFollowing(currentUser.getUsername(), followee.getUsername());
        followRepository.delete(followEntity.get(0));

        return toUnfollowResponse(followee);
    }

    public UnfollowResponse toUnfollowResponse(UserEntity followee) {
        return UnfollowResponse.builder()
                .username(followee.getUsername())
                .bio(followee.getBio())
                .image(followee.getImage())
                .following(false)
                .build();
    }
}
