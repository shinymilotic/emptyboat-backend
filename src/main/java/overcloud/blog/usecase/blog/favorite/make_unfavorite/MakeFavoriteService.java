package overcloud.blog.usecase.blog.favorite.make_unfavorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.*;
import overcloud.blog.infrastructure.auth.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;
import overcloud.blog.repository.jparepository.JpaArticleRepository;
import overcloud.blog.repository.jparepository.JpaFavoriteRepository;
import overcloud.blog.usecase.auth.common.UserError;
import overcloud.blog.usecase.blog.common.ArticleError;
import overcloud.blog.usecase.blog.common.AuthorResponse;
import overcloud.blog.usecase.blog.create_article.ArticleResponse;

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
    public ArticleResponse makeFavorite(String slug) {
        FavoriteEntity favoriteEntity = new FavoriteEntity();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        List<ArticleEntity> articleList = articleRepository.findBySlug(slug);
        if (articleList.isEmpty()) {
            throw new InvalidDataException(ApiError.from(ArticleError.ARTICLE_NO_EXISTS));
        }
        ArticleEntity articleEntity = articleList.get(0);
        favoriteEntity.setId(new FavoriteId());
        favoriteEntity.setArticle(articleEntity);
        favoriteEntity.setUser(currentUser);
        favoriteRepository.save(favoriteEntity);

        UserEntity author = articleEntity.getAuthor();

        ArticleResponse articleResponse = new ArticleResponse();
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setUsername(author.getUsername());
        authorResponse.setBio(author.getBio());
        authorResponse.setImage(author.getImage());

        articleResponse.setAuthor(authorResponse);
        articleResponse.setFavoritesCount(articleEntity.getFavorites().size() + 1);
        articleResponse.setFavorited(true);
        articleResponse.setBody(articleEntity.getBody());
        articleResponse.setDescription(articleEntity.getDescription());

        List<ArticleTag> articleTagList = articleEntity.getArticleTags();
        List<String> tagList = new ArrayList<>();
        for (ArticleTag articleTag : articleTagList) {
            tagList.add(articleTag.getTag().getName());
        }
        articleResponse.setTagList(tagList);
        articleResponse.setId(articleEntity.getId().toString());
        articleResponse.setSlug(articleEntity.getSlug());
        articleResponse.setTitle(articleEntity.getTitle());

        return articleResponse;
    }
}
