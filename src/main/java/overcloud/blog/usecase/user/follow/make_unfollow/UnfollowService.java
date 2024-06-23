package overcloud.blog.usecase.user.follow.make_unfollow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IFollowRepository;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.usecase.user.follow.core.FollowEntity;
import java.util.List;

@Service
public class UnfollowService {

    private final IUserRepository userRepository;
    private final SpringAuthenticationService authenticationService;
    private final IFollowRepository followRepository;
    private final ResFactory resFactory;

    public UnfollowService(IUserRepository userRepository,
                           SpringAuthenticationService authenticationService,
                           IFollowRepository followRepository,
                           ResFactory resFactory) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.followRepository = followRepository;
        this.resFactory = resFactory;
    }

    @Transactional
    public RestResponse<UnfollowResponse> unfollowUser(String username) {
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();
        UserEntity followee = userRepository.findByUsername(username);
        List<FollowEntity> followEntity = followRepository.getFollowing(currentUser.getUsername(), followee.getUsername());
        followRepository.delete(followEntity.get(0));

        return resFactory.success(UserResMsg.USER_UNFOLLOW_SUCCESS, toUnfollowResponse(followee));
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
