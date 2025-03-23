package overcloud.blog.usecase.user.follow.make_follow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.FollowRepository;
import overcloud.blog.repository.UserRepository;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.entity.FollowEntity;
import overcloud.blog.entity.FollowId;
import overcloud.blog.utils.validation.ObjectsValidator;

@Service
public class FollowService {
    private final UserRepository userRepository;
    private final SpringAuthenticationService authenticationService;
    private final FollowRepository followRepository;
    private final ObjectsValidator validator;

    public FollowService(UserRepository userRepository,
                         SpringAuthenticationService authenticationService,
                         FollowRepository followRepository,
                         ObjectsValidator validator) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.followRepository = followRepository;
        this.validator = validator;
    }

    @Transactional
    public Void followUser(String username) {
        FollowEntity followEntity = new FollowEntity();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> validator.fail(UserResMsg.USER_NOT_FOUND))
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
