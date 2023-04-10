package overcloud.blog.application.article.api.service;

import org.springframework.stereotype.Service;
import overcloud.blog.application.article.api.dto.get.ArticleResponse;
import overcloud.blog.application.article.api.dto.get.GetArticleAuthorResponse;
import overcloud.blog.application.article.api.dto.get.GetArticlesResponse;
import overcloud.blog.application.article.api.repository.ArticleRepository;
import overcloud.blog.application.article.favorite.utils.FavoriteUtils;
import overcloud.blog.application.follow.utils.FollowUtils;
import overcloud.blog.domain.ArticleTag;
import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.user.UserEntity;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetArticleService {

    private final FollowUtils followUtils;

    private final FavoriteUtils favoriteUtils;

    private final ArticleRepository articleRepository;

    private final SpringAuthenticationService authenticationService;

    public GetArticleService(FollowUtils followUtils,
                             FavoriteUtils favoriteUtils,
                             ArticleRepository articleRepository,
                             SpringAuthenticationService authenticationService) {
        this.followUtils = followUtils;
        this.favoriteUtils = favoriteUtils;
        this.articleRepository = articleRepository;
        this.authenticationService = authenticationService;
    }

    public GetArticlesResponse getArticle(String slug) {
        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        ArticleEntity articleEntity = articleRepository.findBySlug(slug).get(0);
        UserEntity securityUser = authenticationService.getCurrentUser().get().getUser().get();


        ArticleResponse articleResponse = new ArticleResponse();
        articleResponse.setId(articleEntity.getId().toString());
        articleResponse.setBody(articleEntity.getBody());
        articleResponse.setDescription(articleEntity.getDescription());
        articleResponse.setSlug(articleEntity.getSlug());
        articleResponse.setFavorited(favoriteUtils.isFavorited(securityUser, articleEntity));
        articleResponse.setFavoritesCount(articleEntity.getFavorites().size());
        articleResponse.setTitle(articleEntity.getTitle());
        articleResponse.setCreatedAt(articleEntity.getCreateAt());
        articleResponse.setUpdatedAt(articleEntity.getUpdatedAt());

        List<ArticleTag> articleTagList = articleEntity.getArticleTags();
        List<String> tagList = new ArrayList<>();
        for (ArticleTag articleTag : articleTagList) {
            tagList.add(articleTag.getTag().getName());
        }
        articleResponse.setTagList(tagList);

        GetArticleAuthorResponse articleAuthorResponse = new GetArticleAuthorResponse();
        UserEntity authorEntity = articleEntity.getAuthor();
        articleAuthorResponse.setUsername(authorEntity.getUsername());
        articleAuthorResponse.setFollowing(followUtils.isFollowing(securityUser, authorEntity));
        articleAuthorResponse.setFollowersCount(followUtils.getFollowingCount(authorEntity));
        articleAuthorResponse.setBio(authorEntity.getBio());
        articleAuthorResponse.setImage(authorEntity.getImage());

        articleResponse.setAuthor(articleAuthorResponse);
        getArticlesResponse.setArticles(List.of(articleResponse));

        return getArticlesResponse;
    }
}
