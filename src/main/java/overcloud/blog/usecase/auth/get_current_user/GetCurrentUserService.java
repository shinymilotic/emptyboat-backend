package overcloud.blog.usecase.auth.get_current_user;

import org.springframework.stereotype.Service;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.usecase.auth.common.UserError;
import overcloud.blog.usecase.auth.common.UserResponse;
import overcloud.blog.usecase.auth.common.UserResponseMapper;

@Service
public class GetCurrentUserService {

    private final SpringAuthenticationService authenticationService;

    private final UserResponseMapper userResponseMapper;


    public GetCurrentUserService(SpringAuthenticationService authenticationService,
                                 UserResponseMapper userResponseMapper) {
        this.authenticationService = authenticationService;
        this.userResponseMapper = userResponseMapper;
    }

    public UserResponse getCurrentUser() {
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        return userResponseMapper.toUserResponse(currentUser);
    }
}
