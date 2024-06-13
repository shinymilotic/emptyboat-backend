package overcloud.blog.usecase.auth.get_users;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.usecase.auth.common.UserListResponse;
import overcloud.blog.usecase.auth.common.UserResponse;
import overcloud.blog.usecase.auth.common.UserResponseMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetUserListService {

    private final IUserRepository userRepository;

    private final UserResponseMapper userResponseMapper;

    public GetUserListService(IUserRepository userRepository,
                              UserResponseMapper userResponseMapper) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
    }

    @Transactional(readOnly = true)
    public UserListResponse getUsers(int page, int size) {
        List<UserEntity> users = userRepository.findAll(page, size);
        List<UserResponse> userResponses = users.stream()
                .map(userResponseMapper::toUserResponse)
                .collect(Collectors.toList());

        return UserListResponse.builder()
                .users(userResponses)
                .build();
    }
}
