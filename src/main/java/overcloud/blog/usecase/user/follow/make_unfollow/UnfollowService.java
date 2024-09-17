package overcloud.blog.usecase.user.follow.make_unfollow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.common.auth.service.SpringAuthenticationService;
import overcloud.blog.common.exceptionhandling.InvalidDataException;
import overcloud.blog.common.response.ResFactory;
import overcloud.blog.common.response.RestResponse;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IFollowRepository;
import overcloud.blog.usecase.user.common.UserResMsg;

@Service
public class UnfollowService {
    private final SpringAuthenticationService authenticationService;
    private final IFollowRepository followRepository;
    private final ResFactory resFactory;

    public UnfollowService(SpringAuthenticationService authenticationService,
                           IFollowRepository followRepository,
                           ResFactory resFactory) {
        this.authenticationService = authenticationService;
        this.followRepository = followRepository;
        this.resFactory = resFactory;
    }

    @Transactional
    public RestResponse<Void> unfollowUser(String username) {
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();
        followRepository.unfollow(currentUser.getUsername(), username);

        return resFactory.success(UserResMsg.USER_UNFOLLOW_SUCCESS, null);
    }
}
