package overcloud.blog.usecase.admin.user.get_users;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.UserRepository;
import overcloud.blog.usecase.user.common.UserResponse;
import overcloud.blog.usecase.user.common.UserResponseMapper;
import overcloud.blog.utils.validation.ObjectsValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetUserListService {
    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final ObjectsValidator validator;

    public GetUserListService(UserRepository userRepository,
                              UserResponseMapper userResponseMapper,
                              ObjectsValidator validator) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.validator = validator;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers(int pageNumber, int itemsPerPage) {
        List<UserEntity> users = userRepository.findAll(pageNumber, itemsPerPage);

        return users.stream()
            .map(userResponseMapper::toUserResponse)
            .collect(Collectors.toList());
    }
}
