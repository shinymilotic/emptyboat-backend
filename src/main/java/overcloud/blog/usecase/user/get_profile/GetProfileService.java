package overcloud.blog.usecase.user.get_profile;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.auth.bean.SecurityUser;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.usecase.user.common.UserResMsg;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetProfileService {
    private final IUserRepository userRepository;
    private final SpringAuthenticationService authenticationService;
    private final ResFactory resFactory;

    public GetProfileService(IUserRepository userRepository,
                             SpringAuthenticationService authenticationService,
                             ResFactory resFactory) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.resFactory = resFactory;
    }

    @Transactional(readOnly = true)
    public RestResponse<GetProfileResponse> getProfile(String username) {
        Optional<SecurityUser> currentSecurityUser = authenticationService.getCurrentUser();
        UserEntity currentUser = null;
        if (currentSecurityUser.isPresent()) {
            currentUser = currentSecurityUser.get().getUser();
        }

        UUID currentUserId = null;
        if (currentUser != null) {
            currentUserId = currentUser.getUserId();
        }

        GetProfileResponse user = userRepository.findProfile(username, currentUserId);

        return resFactory.success(UserResMsg.USER_GET_PROFILE, user);
    }

}
