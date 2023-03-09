package overcloud.blog.application.article.favorite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.application.article.api.repository.ArticleRepository;
import overcloud.blog.application.article.favorite.dto.ArticleAuthorResponse;
import overcloud.blog.application.article.favorite.dto.SingleArticleResponse;
import overcloud.blog.application.article.favorite.repository.FavoriteRepository;
import overcloud.blog.application.follow.utils.FollowUtils;
import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.article.favorite.FavoriteEntity;
import overcloud.blog.domain.user.UserEntity;
import overcloud.blog.domain.user.follow.FollowEntity;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.util.List;
import java.util.Set;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private SpringAuthenticationService authenticationService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private FollowUtils followUtils;

    @Transactional
    public SingleArticleResponse makeFavorite(String slug) {
        FavoriteEntity favoriteEntity = new FavoriteEntity();
        UserEntity currenUser = authenticationService.getCurrentUser().get().getUser().get();
        ArticleEntity articleEntity = articleRepository.findBySlug(slug).get(0);
        favoriteEntity.setArticle(articleEntity);
        favoriteEntity.setUser(currenUser);
        UserEntity author = articleEntity.getAuthor();
        favoriteRepository.save(favoriteEntity);

        SingleArticleResponse articleResponse = new SingleArticleResponse();
        ArticleAuthorResponse authorResponse = new ArticleAuthorResponse();
        authorResponse.setUsername(author.getUsername());
        authorResponse.setBio(author.getBio());
        authorResponse.setFollowing(followUtils.isFollowing(currenUser, author));
        authorResponse.setImage(author.getImage());
        authorResponse.setEmail(author.getEmail());

        articleResponse.setAuthor(authorResponse);
        articleResponse.setFavoritesCount(articleEntity.getFavorites().size());
        articleResponse.setFavorited(true);
        articleResponse.setBody(articleEntity.getBody());
        articleResponse.setDescription(articleEntity.getDescription());
        articleResponse.setId(articleEntity.getId().toString());
        articleResponse.setCreatedAt(articleEntity.getCreateAt());
        articleResponse.setUpdatedAt(articleEntity.getUpdatedAt());
        articleResponse.setSlug(articleEntity.getSlug());
        articleResponse.setTitle(articleEntity.getTitle());

        return articleResponse;
    }

    @Transactional
    public SingleArticleResponse makeUnfavorite(String slug) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug).get(0);
        UserEntity author = articleEntity.getAuthor();
        UserEntity currenUser = authenticationService.getCurrentUser().get().getUser().get();

        SingleArticleResponse articleResponse = new SingleArticleResponse();
        ArticleAuthorResponse authorResponse = new ArticleAuthorResponse();
        authorResponse.setUsername(author.getUsername());
        authorResponse.setBio(author.getBio());
        authorResponse.setFollowing(followUtils.isFollowing(currenUser, author));
        authorResponse.setImage(author.getImage());
        authorResponse.setEmail(author.getEmail());

        articleResponse.setAuthor(authorResponse);
        articleResponse.setFavoritesCount(articleEntity.getFavorites().size());
        articleResponse.setFavorited(false);
        articleResponse.setBody(articleEntity.getBody());
        articleResponse.setDescription(articleEntity.getDescription());
        articleResponse.setId(articleEntity.getId().toString());
        articleResponse.setCreatedAt(articleEntity.getCreateAt());
        articleResponse.setUpdatedAt(articleEntity.getUpdatedAt());
        articleResponse.setSlug(articleEntity.getSlug());
        articleResponse.setTitle(articleEntity.getTitle());

        favoriteRepository.deleteBySlug(slug);

        return articleResponse;
    }

}
