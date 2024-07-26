package overcloud.blog.usecase.user.get_profile;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.usecase.common.auth.bean.SecurityUser;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.user.common.UserResMsg;

import java.util.List;
import java.util.Optional;

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
         = new GetProfileResponse();
        Optional<SecurityUser> currentSecurityUser = authenticationService.getCurrentUser();
        UserEntity currentUser = null;
        if (currentSecurityUser.isPresent()) {
            currentUser = currentSecurityUser.get().getUser();
        }

        List<Tuple> user = userRepository.findProfile(username, currentUser.getId());
        if (user == null || user.isEmpty()) {
            throw new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND));
        }
        
        GetProfileResponse profileResponse = getProfileResponse(user);

        return resFactory.success(UserResMsg.USER_GET_PROFILE, profileResponse);
    }

    private GetProfileResponse getProfileResponse(List<Tuple> users) {
        for (Tuple user: users) {

        }

        return null;
    }
}
