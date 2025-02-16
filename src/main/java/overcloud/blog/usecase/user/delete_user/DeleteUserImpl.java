package overcloud.blog.usecase.user.delete_user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.repository.*;
import overcloud.blog.response.ResFactory;
import overcloud.blog.usecase.user.common.UserResMsg;

import java.util.UUID;

@Service
public class DeleteUserImpl implements IDeleteUser {
    private final IUserRepository userRepository;
    private final IRefreshTokenRepository refreshTokenRepository;
    private final IArticleRepository articleRepository;
    private final ITestRepository testRepository;
    private final IUserRoleRepository userRoleRepository;
    private final ITagFollowRepository tagFollowRepository;
    private final IFollowRepository followRepository;
    private final SpringAuthenticationService authenticationService;
    private final ResFactory resFactory;

    public DeleteUserImpl(IUserRepository userRepository,
                          IRefreshTokenRepository refreshTokenRepository,
                          IArticleRepository articleRepository,
                          ITestRepository testRepository,
                          IUserRoleRepository userRoleRepository,
                          ITagFollowRepository tagFollowRepository,
                          IFollowRepository followRepository,
                          SpringAuthenticationService authenticationService,
                          ResFactory resFactory) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.articleRepository = articleRepository;
        this.testRepository = testRepository;
        this.userRoleRepository = userRoleRepository;
        this.tagFollowRepository = tagFollowRepository;
        this.followRepository = followRepository;
        this.authenticationService = authenticationService;
        this.resFactory = resFactory;
    }

    @Override
    @Transactional
    public Void deleteUser(String userId) {
        UUID uuidUserId = UUID.fromString(userId);

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> resFactory.fail(UserResMsg.USER_NOT_FOUND))
                .getUser();

        if (currentUser.getUserId().equals(uuidUserId)) {
            throw resFactory.fail(UserResMsg.USER_DELETE_CURRENT);
        }

        refreshTokenRepository.deleteByUserId(uuidUserId);
        articleRepository.deleteByUserId(uuidUserId);
        testRepository.deleteByUserId(uuidUserId);
        userRoleRepository.deleteByUserId(uuidUserId);
        tagFollowRepository.deleteByUserId(uuidUserId);
        followRepository.deleteByUserId(uuidUserId);
        userRepository.deleteUser(uuidUserId);
        return null;
    }
}
