package overcloud.blog.application.article.get_article;

import org.springframework.stereotype.Service;
import overcloud.blog.application.article.core.repository.ArticleRepository;
import overcloud.blog.application.article.favorite.core.repository.FavoriteRepository;
import overcloud.blog.application.article.favorite.core.utils.FavoriteUtils;
import overcloud.blog.application.user.follow.core.utils.FollowUtils;
import overcloud.blog.application.article_tag.ArticleTag;
import overcloud.blog.application.article.core.ArticleEntity;
import overcloud.blog.application.article.favorite.core.FavoriteEntity;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GetArticleService {

    private final FollowUtils followUtils;

    private final FavoriteUtils favoriteUtils;

    private final ArticleRepository articleRepository;

    private final SpringAuthenticationService authenticationService;

    private final FavoriteRepository favoriteRepository;

    public GetArticleService(FollowUtils followUtils,
                             FavoriteUtils favoriteUtils,
                             ArticleRepository articleRepository,
                             SpringAuthenticationService authenticationService,
                             FavoriteRepository favoriteRepository) {
        this.followUtils = followUtils;
        this.favoriteUtils = favoriteUtils;
        this.articleRepository = articleRepository;
        this.authenticationService = authenticationService;
        this.favoriteRepository = favoriteRepository;
    }

    public GetArticleResponse getArticle(String slug) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug).get(0);
        UserEntity currentUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .orElseGet(() -> Optional.empty())
                .orElseGet(() -> null);

        GetArticleResponse articleResponse = new GetArticleResponse();
        articleResponse.setId(articleEntity.getId().toString());
        articleResponse.setBody(articleEntity.getBody());
        articleResponse.setDescription(articleEntity.getDescription());
        articleResponse.setSlug(articleEntity.getSlug());

        if (currentUser != null) {
            List<FavoriteEntity> favorites = favoriteRepository.findById(currentUser.getId(), articleEntity.getId());
            articleEntity.setFavorites(favorites);
            articleResponse.setFavorited(favoriteUtils.isFavorited(Optional.of(currentUser), articleEntity));
        }

        articleResponse.setFavoritesCount(articleEntity.getFavorites().size());
        articleResponse.setTitle(articleEntity.getTitle());
        articleResponse.setCreatedAt(articleEntity.getCreatedAt());
        articleResponse.setUpdatedAt(articleEntity.getUpdatedAt());

        List<ArticleTag> articleTagList = articleEntity.getArticleTags();
        List<String> tagList = new ArrayList<>();
        for (ArticleTag articleTag : articleTagList) {
            tagList.add(articleTag.getTag().getName());
        }
        articleResponse.setTagList(tagList);

        AuthorResponse articleAuthorResponse = new AuthorResponse();
        UserEntity authorEntity = articleEntity.getAuthor();
        articleAuthorResponse.setUsername(authorEntity.getUsername());
        if(currentUser != null) {
            articleAuthorResponse.setFollowing(followUtils.isFollowing(Optional.of(currentUser), authorEntity));
        }
        articleAuthorResponse.setFollowersCount(followUtils.getFollowingCount(authorEntity));
        articleAuthorResponse.setBio(authorEntity.getBio());
        articleAuthorResponse.setImage(authorEntity.getImage());

        articleResponse.setAuthor(articleAuthorResponse);

        return articleResponse;
    }
}
