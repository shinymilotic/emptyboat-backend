package overcloud.blog.usecase.user.follow.make_follow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ResFactory;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IFollowRepository;
import overcloud.blog.repository.IUserRepository;
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
    public Void followUser(String username) {
        FollowEntity followEntity = new FollowEntity();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();

        UserEntity followee = userRepository.findByUsername(username);
        FollowId followId = new FollowId();
        followId.setFolloweeId(followee.getUserId());
        followId.setFollowerId(currentUser.getUserId());
        followEntity.setId(followId);
        followRepository.save(followEntity);

        return null;
    }
}
