package overcloud.blog.usecase.blog.favorite.make_favorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.*;
import overcloud.blog.repository.ArticleRepository;
import overcloud.blog.repository.FavoriteRepository;
import overcloud.blog.usecase.blog.common.ArticleResMsg;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;

import java.util.Optional;
import java.util.UUID;

@Service
public class MakeFavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final SpringAuthenticationService authenticationService;
    private final ArticleRepository articleRepository;
    private final ObjectsValidator validator;

    public MakeFavoriteService(FavoriteRepository favoriteRepository,
                               SpringAuthenticationService authenticationService,
                               ArticleRepository articleRepository,
                               ObjectsValidator validator) {
        this.favoriteRepository = favoriteRepository;
        this.authenticationService = authenticationService;
        this.articleRepository = articleRepository;
        this.validator = validator;
    }

    @Transactional
    public Void makeFavorite(String id) {
        FavoriteEntity favoriteEntity = new FavoriteEntity();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> validator.fail(UserResMsg.USER_NOT_FOUND))
                .getUser();

        Optional<ArticleEntity> articleList = articleRepository.findById(UUID.fromString(id));
        if (articleList.isEmpty()) {
            throw validator.fail(ArticleResMsg.ARTICLE_NO_EXISTS);
        }
        ArticleEntity articleEntity = articleList.get();
        FavoriteId favoriteId = new FavoriteId();
        favoriteId.setArticleId(articleEntity.getArticleId());
        favoriteId.setUserId(currentUser.getUserId());
        favoriteEntity.setId(favoriteId);
        favoriteRepository.save(favoriteEntity);

        return null;
    }
}
