package overcloud.blog.usecase.user.follow.make_follow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.jparepository.JpaFollowRepository;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.exceptionhandling.ApiError;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.user.common.UserError;
import overcloud.blog.usecase.user.follow.core.FollowEntity;
import overcloud.blog.usecase.user.follow.core.FollowId;
import overcloud.blog.usecase.user.follow.core.utils.FollowUtils;

@Service
public class FollowService {

    private final JpaUserRepository userRepository;

    private final SpringAuthenticationService authenticationService;

    private final JpaFollowRepository followRepository;

    private final FollowUtils followUtils;

    public FollowService(JpaUserRepository userRepository,
                         SpringAuthenticationService authenticationService,
                         JpaFollowRepository followRepository,
                         FollowUtils followUtils) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.followRepository = followRepository;
        this.followUtils = followUtils;
    }

    @Transactional
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
