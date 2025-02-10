package overcloud.blog.usecase.user.delete_user;

import org.springframework.stereotype.Service;
import overcloud.blog.repository.*;

import java.util.UUID;

@Service
public class DeleteUserImpl implements IDeleteUser {
    private final IUserRepository userRepository;
    private final IRefreshTokenRepository refreshTokenRepository;
    private final IArticleRepository articleRepository;
    private final ITestRepository testRepository;
    private final IRoleRepository roleRepository;
    private final ITagFollowRepository tagFollowRepository;

    public DeleteUserImpl(IUserRepository userRepository, IRefreshTokenRepository refreshTokenRepository, IArticleRepository articleRepository, ITestRepository testRepository, IRoleRepository roleRepository, ITagFollowRepository tagFollowRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.articleRepository = articleRepository;
        this.testRepository = testRepository;
        this.roleRepository = roleRepository;
        this.tagFollowRepository = tagFollowRepository;
    }

    @Override
    public Void deleteUser(String userId) {
        UUID uuidUserId = UUID.fromString(userId);
        refreshTokenRepository.deleteByUserId(uuidUserId);
//        articleRepository.deleteByUserId(uuidUserId);
//        testRepository.deleteByUserId(uuidUserId);
//        roleRepository.deleteByUserId(uuidUserId);
//        tagFollowRepository.deleteByUserId(uuidUserId);
        userRepository.deleteUser(uuidUserId);
        return null;
    }
}
