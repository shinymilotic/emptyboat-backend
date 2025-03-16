package overcloud.blog.usecase.user.delete_user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.*;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;

import java.util.UUID;

@Service
public class DeleteUserImpl implements DeleteUser {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ArticleRepository articleRepository;
    private final TestRepository testRepository;
    private final UserRoleRepository userRoleRepository;
    private final TagFollowRepository tagFollowRepository;
    private final FollowRepository followRepository;
    private final FavoriteRepository favoriteRepository;
    private final SpringAuthenticationService authenticationService;
    private final ObjectsValidator validator;

    public DeleteUserImpl(UserRepository userRepository,
                          RefreshTokenRepository refreshTokenRepository,
                          ArticleRepository articleRepository,
                          TestRepository testRepository,
                          UserRoleRepository userRoleRepository,
                          TagFollowRepository tagFollowRepository,
                          FollowRepository followRepository,
                          FavoriteRepository favoriteRepository,
                          SpringAuthenticationService authenticationService,
                          ObjectsValidator validator) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.articleRepository = articleRepository;
        this.testRepository = testRepository;
        this.userRoleRepository = userRoleRepository;
        this.tagFollowRepository = tagFollowRepository;
        this.followRepository = followRepository;
        this.favoriteRepository = favoriteRepository;
        this.authenticationService = authenticationService;
        this.validator = validator;
    }

    @Override
    @Transactional
    public Void deleteUser(String userId) {
        UUID uuidUserId = UUID.fromString(userId);

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> validator.fail(UserResMsg.USER_NOT_FOUND))
                .getUser();

        if (currentUser.getUserId().equals(uuidUserId)) {
            throw validator.fail(UserResMsg.USER_DELETE_CURRENT);
        }

        refreshTokenRepository.deleteByUserId(uuidUserId);
        favoriteRepository.deleteByUserId(uuidUserId);
        articleRepository.deleteByUserId(uuidUserId);
        testRepository.deleteByUserId(uuidUserId);
        userRoleRepository.deleteByUserId(uuidUserId);
        tagFollowRepository.deleteByUserId(uuidUserId);
        followRepository.deleteByUserId(uuidUserId);
        userRepository.deleteUser(uuidUserId);
        return null;
    }
}
