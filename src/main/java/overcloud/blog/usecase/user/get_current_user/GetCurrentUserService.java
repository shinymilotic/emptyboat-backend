package overcloud.blog.usecase.user.get_current_user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.usecase.user.common.UserResponse;
import overcloud.blog.usecase.user.common.UserResponseMapper;

@Service
public class GetCurrentUserService {
    private final SpringAuthenticationService authenticationService;
    private final UserResponseMapper userResponseMapper;
    private final ResFactory resFactory;

    public GetCurrentUserService(SpringAuthenticationService authenticationService,
                                 UserResponseMapper userResponseMapper,
                                 ResFactory resFactory) {
        this.authenticationService = authenticationService;
        this.userResponseMapper = userResponseMapper;
        this.resFactory = resFactory;
    }

    @Transactional(readOnly = true)
    public RestResponse<UserResponse> getCurrentUser() {
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();

        return resFactory.success(UserResMsg.USER_GET_CURRENT_USER, userResponseMapper.toUserResponse(currentUser));
    }
}
