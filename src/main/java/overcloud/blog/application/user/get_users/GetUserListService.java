package overcloud.blog.application.user.get_users;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.UserRepository;
import overcloud.blog.application.user.core.UserListResponse;
import overcloud.blog.application.user.core.UserResponse;
import overcloud.blog.application.user.core.UserResponseMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetUserListService {

    private final UserRepository userRepository;

    private final UserResponseMapper userResponseMapper;
    public GetUserListService(UserRepository userRepository,
                       UserResponseMapper userResponseMapper) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
    }

    @Transactional
    public UserListResponse getUsers(int page, int size) {
        List<UserEntity> users = userRepository.findAll(page, size);
        List<UserResponse> userResponses =  users.stream()
                .map(userResponseMapper::toUserResponse)
                .collect(Collectors.toList());

        return UserListResponse.builder()
                .users(userResponses)
                .build();
    }
}
