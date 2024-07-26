package overcloud.blog.usecase.user.follow.make_follow;

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
import overcloud.blog.usecase.user.follow.core.FollowId;

@Service
public class FollowService {
    private final IUserRepository userRepository;
    private final SpringAuthenticationService authenticationService;
    private final IFollowRepository followRepository;
    private final ResFactory resFactory;

    public FollowService(IUserRepository userRepository,
                         SpringAuthenticationService authenticationService,
                         IFollowRepository followRepository,
                         ResFactory resFactory) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.followRepository = followRepository;
        this.resFactory = resFactory;
    }

    @Transactional
    public RestResponse<Void> followUser(String username) {
        FollowEntity followEntity = new FollowEntity();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();

        UserEntity followee = userRepository.findByUsername(username);
        FollowId followId = new FollowId();
        followId.setFolloweeId(followee.getId());
        followId.setFollowerId(currentUser.getId());
        followEntity.setId(followId);
        followEntity.setFollower(currentUser);
        followEntity.setFollowee(followee);
        followRepository.save(followEntity);

        return resFactory.success(UserResMsg.USER_FOLLOW_SUCCESS, null) ;
    }
}
