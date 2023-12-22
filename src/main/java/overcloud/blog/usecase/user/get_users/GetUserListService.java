package overcloud.blog.usecase.user.get_users;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.usecase.user.core.UserListResponse;
import overcloud.blog.usecase.user.core.UserResponse;
import overcloud.blog.usecase.user.core.UserResponseMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetUserListService {

    private final JpaUserRepository userRepository;

    private final UserResponseMapper userResponseMapper;
    public GetUserListService(JpaUserRepository userRepository,
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
