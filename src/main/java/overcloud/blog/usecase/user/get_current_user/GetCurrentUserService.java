package overcloud.blog.usecase.user.get_current_user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.usecase.user.common.UserResponse;
import overcloud.blog.usecase.user.common.UserResponseMapper;
import overcloud.blog.utils.validation.ObjectsValidator;

@Service
public class GetCurrentUserService {
    private final SpringAuthenticationService authenticationService;
    private final UserResponseMapper userResponseMapper;
    private final ObjectsValidator validator;

    public GetCurrentUserService(SpringAuthenticationService authenticationService,
                                 UserResponseMapper userResponseMapper,
                                 ObjectsValidator validator) {
        this.authenticationService = authenticationService;
        this.userResponseMapper = userResponseMapper;
        this.validator = validator;
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> validator.fail(UserResMsg.USER_NOT_FOUND))
                .getUser();

        return userResponseMapper.toUserResponse(currentUser);
    }
}
