package overcloud.blog.usecase.admin.user.get_users;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.UserRepository;
import overcloud.blog.usecase.user.common.UserResponse;
import overcloud.blog.usecase.user.common.UserResponseMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetUserListServiceImpl implements GetUserListService {
    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;

    public GetUserListServiceImpl(UserRepository userRepository,
                                  UserResponseMapper userResponseMapper) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
    }

    @Transactional(readOnly = true)
    public UserListResponse getUsers(int pageNumber, int itemsPerPage) {
        List<UserEntity> users = userRepository.findAll(pageNumber, itemsPerPage);
        UserListResponse response = new UserListResponse();

        List<UserResponse> userList = users.stream()
            .map(userResponseMapper::toUserResponse)
            .collect(Collectors.toList());
        response.setUsers(userList);
        response.setUserCount(userList.size());

        return response;
    }
}
