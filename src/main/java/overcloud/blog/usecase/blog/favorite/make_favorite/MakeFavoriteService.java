package overcloud.blog.usecase.blog.favorite.make_favorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.*;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.repository.IFavoriteRepository;
import overcloud.blog.usecase.blog.common.ArticleResMsg;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.util.List;

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
    public RestResponse<Void> makeFavorite(String slug) {
        FavoriteEntity favoriteEntity = new FavoriteEntity();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();

        List<ArticleEntity> articleList = articleRepository.findBySlug(slug);
        if (articleList.isEmpty()) {
            throw new InvalidDataException(resFactory.fail(ArticleResMsg.ARTICLE_NO_EXISTS));
        }
        ArticleEntity articleEntity = articleList.getFirst();
        favoriteEntity.setId(new FavoriteId());
        favoriteEntity.setArticle(articleEntity);
        favoriteEntity.setUser(currentUser);
        favoriteRepository.save(favoriteEntity);

        return resFactory.success(ArticleResMsg.ARTICLE_FAVORITE_SUCCESS, null);
    }
}
