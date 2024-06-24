package overcloud.blog.usecase.user.get_profile;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.usecase.common.auth.bean.SecurityUser;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.usecase.user.follow.core.utils.FollowUtils;
import java.util.Optional;

@Service
public class GetProfileService {
    private final IUserRepository userRepository;
    private final SpringAuthenticationService authenticationService;
    private final FollowUtils followUtils;
    private final ResFactory resFactory;

    public GetProfileService(IUserRepository userRepository,
                             SpringAuthenticationService authenticationService,
                             FollowUtils followUtils,
                             ResFactory resFactory) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.followUtils = followUtils;
        this.resFactory = resFactory;
    }

    @Transactional(readOnly = true)
    public RestResponse<GetProfileResponse> getProfile(String username) {
        GetProfileResponse profileResponse = new GetProfileResponse();
        Optional<SecurityUser> currentSecurityUser = authenticationService.getCurrentUser();
        UserEntity currentUser = null;
        if (currentSecurityUser.isPresent()) {
            currentUser = currentSecurityUser.get().getUser();
        }

        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND));
        }

        profileResponse.setUsername(user.getUsername());
        profileResponse.setEmail(user.getEmail());
        profileResponse.setFollowing(followUtils.isFollowing(currentUser, user));
        profileResponse.setBio(user.getBio());
        profileResponse.setImage(user.getImage());
        profileResponse.setFollowersCount(followUtils.getFollowingCount(user));

        return resFactory.success(UserResMsg.USER_GET_PROFILE, profileResponse);
    }
}
