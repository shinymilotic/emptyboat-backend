package overcloud.blog.application.article.favorite.make_unfavorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.application.article.core.ArticleEntity;
import overcloud.blog.application.article.core.exception.InvalidDataException;
import overcloud.blog.application.article.core.repository.ArticleRepository;
import overcloud.blog.application.article.favorite.core.FavoriteEntity;
import overcloud.blog.application.article.favorite.core.FavoriteId;
import overcloud.blog.application.article.favorite.core.dto.ArticleAuthorResponse;
import overcloud.blog.application.article.favorite.core.dto.SingleArticleResponse;
import overcloud.blog.application.article.favorite.core.repository.FavoriteRepository;
import overcloud.blog.application.article_tag.core.ArticleTag;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserError;
import overcloud.blog.application.user.follow.core.utils.FollowUtils;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.util.ArrayList;
import java.util.List;

@Service
public class MakeFavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final SpringAuthenticationService authenticationService;

    private final ArticleRepository articleRepository;

    private final FollowUtils followUtils;

    public MakeFavoriteService(FavoriteRepository favoriteRepository,
                           SpringAuthenticationService authenticationService,
                           ArticleRepository articleRepository,
                           FollowUtils followUtils) {
        this.favoriteRepository = favoriteRepository;
        this.authenticationService = authenticationService;
        this.articleRepository = articleRepository;
        this.followUtils = followUtils;
    }

    @Transactional
    public SingleArticleResponse makeFavorite(String slug) {
        FavoriteEntity favoriteEntity = new FavoriteEntity();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        ArticleEntity articleEntity = articleRepository.findBySlug(slug).get(0);
        favoriteEntity.setId(new FavoriteId());
        favoriteEntity.setArticle(articleEntity);
        favoriteEntity.setUser(currentUser);
        UserEntity author = articleEntity.getAuthor();
        favoriteRepository.save(favoriteEntity);


        SingleArticleResponse articleResponse = new SingleArticleResponse();
        ArticleAuthorResponse authorResponse = new ArticleAuthorResponse();
        authorResponse.setUsername(author.getUsername());
        authorResponse.setBio(author.getBio());
        authorResponse.setFollowing(followUtils.isFollowing(currentUser, author));
        authorResponse.setFollowersCount(followUtils.getFollowingCount(author));
        authorResponse.setImage(author.getImage());
        authorResponse.setEmail(author.getEmail());

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
        articleResponse.setCreatedAt(articleEntity.getCreatedAt());
        articleResponse.setUpdatedAt(articleEntity.getUpdatedAt());
        articleResponse.setSlug(articleEntity.getSlug());
        articleResponse.setTitle(articleEntity.getTitle());

        return articleResponse;
    }
}
