package overcloud.blog.usecase.user.follow.make_follow;

import org.springframework.stereotype.Service;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.usecase.user.core.UserError;
import overcloud.blog.usecase.user.follow.core.FollowEntity;
import overcloud.blog.usecase.user.follow.core.FollowId;
import overcloud.blog.usecase.user.follow.core.repository.FollowRepository;
import overcloud.blog.usecase.user.follow.core.utils.FollowUtils;

@Service
public class FollowService {

    private final JpaUserRepository userRepository;

    private final SpringAuthenticationService authenticationService;

    private final FollowRepository followRepository;

    private final FollowUtils followUtils;

    public FollowService(JpaUserRepository userRepository,
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
