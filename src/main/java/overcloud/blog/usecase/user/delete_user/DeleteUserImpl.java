package overcloud.blog.usecase.user.delete_user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.repository.*;

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

    public DeleteUserImpl(IUserRepository userRepository,
                          IRefreshTokenRepository refreshTokenRepository,
                          IArticleRepository articleRepository,
                          ITestRepository testRepository,
                          IUserRoleRepository userRoleRepository,
                          ITagFollowRepository tagFollowRepository,
                          IFollowRepository followRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.articleRepository = articleRepository;
        this.testRepository = testRepository;
        this.userRoleRepository = userRoleRepository;
        this.tagFollowRepository = tagFollowRepository;
        this.followRepository = followRepository;
    }

    @Override
    @Transactional
    public Void deleteUser(String userId) {
        UUID uuidUserId = UUID.fromString(userId);
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
