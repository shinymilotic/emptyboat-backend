package overcloud.blog.usecase.article.get_article;

import org.springframework.stereotype.Service;
import overcloud.blog.repository.jparepository.JpaArticleRepository;
import overcloud.blog.usecase.article.favorite.core.utils.FavoriteUtils;
import overcloud.blog.usecase.user.follow.core.utils.FollowUtils;
import overcloud.blog.entity.ArticleTag;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetArticleService {

    private final FollowUtils followUtils;

    private final FavoriteUtils favoriteUtils;

    private final JpaArticleRepository articleRepository;

    private final SpringAuthenticationService authenticationService;


    public GetArticleService(FollowUtils followUtils,
                             FavoriteUtils favoriteUtils,
                             JpaArticleRepository articleRepository,
                             SpringAuthenticationService authenticationService) {
        this.followUtils = followUtils;
        this.favoriteUtils = favoriteUtils;
        this.articleRepository = articleRepository;
        this.authenticationService = authenticationService;
    }

    public GetArticleResponse getArticle(String slug) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug).get(0);
        SecurityUser securityUser = authenticationService.getCurrentUser()
                .orElse(null);

        UserEntity currentUser = null;
        if(securityUser != null) {
            currentUser = securityUser.getUser();
        }

        GetArticleResponse articleResponse = new GetArticleResponse();
        articleResponse.setId(articleEntity.getId().toString());
        articleResponse.setBody(articleEntity.getBody());
        articleResponse.setDescription(articleEntity.getDescription());
        articleResponse.setSlug(articleEntity.getSlug());

        if (currentUser != null) {
            articleResponse.setFavorited(favoriteUtils.isFavorited(currentUser, articleEntity));
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
            articleAuthorResponse.setFollowing(followUtils.isFollowing(currentUser, authorEntity));
        }
        articleAuthorResponse.setFollowersCount(followUtils.getFollowingCount(authorEntity));
        articleAuthorResponse.setBio(authorEntity.getBio());
        articleAuthorResponse.setImage(authorEntity.getImage());

        articleResponse.setAuthor(articleAuthorResponse);

        return articleResponse;
    }
}
