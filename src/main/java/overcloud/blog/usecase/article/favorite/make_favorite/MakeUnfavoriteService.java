package overcloud.blog.usecase.article.favorite.make_favorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.repository.ArticleRepository;
import overcloud.blog.entity.FavoriteId;
import overcloud.blog.repository.JpaFavoriteRepository;
import overcloud.blog.usecase.article.core.AuthorResponse;
import overcloud.blog.usecase.article.create_article.ArticleResponse;
import overcloud.blog.usecase.user.core.UserError;
import overcloud.blog.entity.ArticleTag;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.util.ArrayList;
import java.util.List;

@Service
public class MakeUnfavoriteService {

    private final JpaFavoriteRepository favoriteRepository;

    private final SpringAuthenticationService authenticationService;

    private final ArticleRepository articleRepository;


    public MakeUnfavoriteService(JpaFavoriteRepository favoriteRepository,
                           SpringAuthenticationService authenticationService,
                           ArticleRepository articleRepository) {
        this.favoriteRepository = favoriteRepository;
        this.authenticationService = authenticationService;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public ArticleResponse makeUnfavorite(String slug) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug).get(0);
        UserEntity author = articleEntity.getAuthor();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        ArticleResponse articleResponse = new ArticleResponse();
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setUsername(author.getUsername());
        authorResponse.setBio(author.getBio());
        authorResponse.setImage(author.getImage());

        articleResponse.setAuthor(authorResponse);
        articleResponse.setFavoritesCount(articleEntity.getFavorites().size() - 1);
        articleResponse.setFavorited(false);
        articleResponse.setBody(articleEntity.getBody());
        List<ArticleTag> articleTagList = articleEntity.getArticleTags();
        List<String> tagList = new ArrayList<>();
        for (ArticleTag articleTag : articleTagList) {
            tagList.add(articleTag.getTag().getName());
        }
        articleResponse.setTagList(tagList);

        articleResponse.setDescription(articleEntity.getDescription());
        articleResponse.setId(articleEntity.getId().toString());
        articleResponse.setSlug(articleEntity.getSlug());
        articleResponse.setTitle(articleEntity.getTitle());

        FavoriteId favoritePk = new FavoriteId();
        favoritePk.setUserId(currentUser.getId());
        favoritePk.setArticleId(articleEntity.getId());
        favoriteRepository.deleteById(favoritePk);
        return articleResponse;
    }
}
