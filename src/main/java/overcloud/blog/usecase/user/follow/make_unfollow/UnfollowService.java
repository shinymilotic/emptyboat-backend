package overcloud.blog.usecase.user.follow.make_unfollow;

import org.springframework.stereotype.Service;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.usecase.user.core.UserError;
import overcloud.blog.usecase.user.follow.core.FollowEntity;
import overcloud.blog.usecase.user.follow.core.repository.FollowRepository;
import overcloud.blog.usecase.user.follow.core.utils.FollowUtils;

import java.util.List;

@Service
public class UnfollowService {

    private JpaUserRepository userRepository;

    private SpringAuthenticationService authenticationService;

    private FollowRepository followRepository;

    private FollowUtils followUtils;

    public UnfollowService(JpaUserRepository userRepository,
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
