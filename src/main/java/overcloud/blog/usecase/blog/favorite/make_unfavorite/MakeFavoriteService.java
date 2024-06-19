package overcloud.blog.usecase.blog.favorite.make_unfavorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.entity.*;
import overcloud.blog.repository.jparepository.JpaArticleRepository;
import overcloud.blog.repository.jparepository.JpaFavoriteRepository;
import overcloud.blog.usecase.blog.common.ArticleResMsg;
import overcloud.blog.usecase.blog.common.AuthorResponse;
import overcloud.blog.usecase.blog.create_article.ArticleResponse;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.exceptionhandling.ApiError;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.user.common.UserError;

import java.util.ArrayList;
import java.util.List;

@Service
public class MakeFavoriteService {

    private final JpaFavoriteRepository favoriteRepository;

    private final SpringAuthenticationService authenticationService;

    private final JpaArticleRepository articleRepository;


    public MakeFavoriteService(JpaFavoriteRepository favoriteRepository,
                               SpringAuthenticationService authenticationService,
                               JpaArticleRepository articleRepository) {
        this.favoriteRepository = favoriteRepository;
        this.authenticationService = authenticationService;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public RestResponse<?> makeFavorite(String slug) {
        FavoriteEntity favoriteEntity = new FavoriteEntity();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        List<ArticleEntity> articleList = articleRepository.findBySlug(slug);
        if (articleList.isEmpty()) {
            throw new InvalidDataException(ApiError.from(ArticleResMsg.ARTICLE_NO_EXISTS));
        }
        ArticleEntity articleEntity = articleList.getFirst();
        favoriteEntity.setId(new FavoriteId());
        favoriteEntity.setArticle(articleEntity);
        favoriteEntity.setUser(currentUser);
        favoriteRepository.save(favoriteEntity);

        return RestResponse.success(null);
    }
}
