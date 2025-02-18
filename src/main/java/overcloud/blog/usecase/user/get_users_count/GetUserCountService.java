package overcloud.blog.usecase.user.get_users_count;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;

@Service
public class GetUserCountService implements IGetUserCountService {
    private final IUserRepository userRepository;
    private final SpringAuthenticationService authenticationService;
    private final ObjectsValidator validator;

    public GetUserCountService(IUserRepository userRepository,
                               SpringAuthenticationService authenticationService,
                               ObjectsValidator validator) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.validator = validator;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUsersCount() {
        Long usersCount = userRepository.getUsersCount();
        authenticationService.getCurrentUser()
                .orElseThrow(() -> validator.fail(UserResMsg.USER_NOT_FOUND));

        return usersCount;
    }
}
