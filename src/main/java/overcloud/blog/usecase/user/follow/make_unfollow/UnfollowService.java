package overcloud.blog.usecase.user.follow.make_unfollow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IFollowRepository;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;

@Service
public class UnfollowService {
    private final SpringAuthenticationService authenticationService;
    private final IFollowRepository followRepository;
    private final ObjectsValidator validator;

    public UnfollowService(SpringAuthenticationService authenticationService,
                           IFollowRepository followRepository,
                           ObjectsValidator validator) {
        this.authenticationService = authenticationService;
        this.followRepository = followRepository;
        this.validator = validator;
    }

    @Transactional
    public Void unfollowUser(String username) {
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> validator.fail(UserResMsg.USER_NOT_FOUND))
                .getUser();
        followRepository.unfollow(currentUser.getUsername(), username);

        return null;
    }
}
