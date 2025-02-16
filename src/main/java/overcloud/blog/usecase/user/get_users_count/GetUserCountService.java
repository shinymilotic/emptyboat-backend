package overcloud.blog.usecase.user.get_users_count;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.response.ResFactory;
import overcloud.blog.usecase.user.common.UserResMsg;

@Service
public class GetUserCountService implements IGetUserCountService {
    private final IUserRepository userRepository;
    private final SpringAuthenticationService authenticationService;
    private final ResFactory resFactory;

    public GetUserCountService(IUserRepository userRepository,
                               SpringAuthenticationService authenticationService,
                               ResFactory resFactory) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.resFactory = resFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUsersCount() {
        Long usersCount = userRepository.getUsersCount();
        authenticationService.getCurrentUser()
                .orElseThrow(() -> resFactory.fail(UserResMsg.USER_NOT_FOUND));

        return usersCount;
    }
}
