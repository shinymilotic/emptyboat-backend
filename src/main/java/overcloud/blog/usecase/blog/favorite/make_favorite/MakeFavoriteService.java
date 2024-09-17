package overcloud.blog.usecase.blog.favorite.make_favorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.common.auth.service.SpringAuthenticationService;
import overcloud.blog.common.exceptionhandling.InvalidDataException;
import overcloud.blog.common.response.ResFactory;
import overcloud.blog.common.response.RestResponse;
import overcloud.blog.entity.*;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.repository.IFavoriteRepository;
import overcloud.blog.usecase.blog.common.ArticleResMsg;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.util.Optional;
import java.util.UUID;

@Service
public class MakeFavoriteService {
    private final IFavoriteRepository favoriteRepository;
    private final SpringAuthenticationService authenticationService;
    private final IArticleRepository articleRepository;
    private final ResFactory resFactory;

    public MakeFavoriteService(IFavoriteRepository favoriteRepository,
                               SpringAuthenticationService authenticationService,
                               IArticleRepository articleRepository,
                               ResFactory resFactory) {
        this.favoriteRepository = favoriteRepository;
        this.authenticationService = authenticationService;
        this.articleRepository = articleRepository;
        this.resFactory = resFactory;
    }

    @Transactional
    public RestResponse<Void> makeFavorite(String id) {
        FavoriteEntity favoriteEntity = new FavoriteEntity();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();

        Optional<ArticleEntity> articleList = articleRepository.findById(UUID.fromString(id));
        if (!articleList.isPresent()) {
            throw new InvalidDataException(resFactory.fail(ArticleResMsg.ARTICLE_NO_EXISTS));
        }
        ArticleEntity articleEntity = articleList.get();
        FavoriteId favoriteId = new FavoriteId();
        favoriteId.setArticleId(articleEntity.getArticleId());
        favoriteId.setUserId(currentUser.getUserId());
        favoriteEntity.setId(favoriteId);
        favoriteRepository.save(favoriteEntity);

        return resFactory.success(ArticleResMsg.ARTICLE_FAVORITE_SUCCESS, null);
    }
}
