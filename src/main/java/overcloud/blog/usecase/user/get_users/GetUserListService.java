package overcloud.blog.usecase.user.get_users;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.usecase.user.common.UserResponse;
import overcloud.blog.usecase.user.common.UserResponseMapper;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetUserListService {
    private final IUserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final ResFactory resFactory;

    public GetUserListService(IUserRepository userRepository,
                              UserResponseMapper userResponseMapper,
                              ResFactory resFactory) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.resFactory = resFactory;
    }

    @Transactional(readOnly = true)
    public RestResponse<List<UserResponse>> getUsers(int page, int size) {
        List<UserEntity> users = userRepository.findAll(page, size);
        List<UserResponse> userResponses = users.stream()
                .map(userResponseMapper::toUserResponse)
                .collect(Collectors.toList());

        return resFactory.success(UserResMsg.USER_GET_USER_LIST, userResponses); 
    }
}
