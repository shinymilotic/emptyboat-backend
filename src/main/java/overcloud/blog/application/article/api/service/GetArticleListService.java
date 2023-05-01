package overcloud.blog.application.article.api.service;

import org.springframework.stereotype.Service;
import overcloud.blog.application.article.api.dto.get.multiple.GetArticlesSingleResponse;
import overcloud.blog.application.article.api.dto.get.multiple.GetArticlesAuthorResponse;
import overcloud.blog.application.article.api.dto.get.multiple.GetArticlesResponse;
import overcloud.blog.application.article.api.repository.ArticleRepository;
import overcloud.blog.application.article.favorite.utils.FavoriteUtils;
import overcloud.blog.application.follow.utils.FollowUtils;
import overcloud.blog.domain.article.ArticleTag;
import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.article.favorite.FavoriteEntity;
import overcloud.blog.domain.user.UserEntity;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GetArticleListService {

    private final FollowUtils followUtils;

    private final FavoriteUtils favoriteUtils;

    private final ArticleRepository articleRepository;

    private final SpringAuthenticationService authenticationService;

    public GetArticleListService(FollowUtils followUtils,
                                 FavoriteUtils favoriteUtils,
                                 ArticleRepository articleRepository,
                                 SpringAuthenticationService authenticationService) {
        this.followUtils = followUtils;
        this.favoriteUtils = favoriteUtils;
        this.articleRepository = articleRepository;
        this.authenticationService = authenticationService;
    }

    public GetArticlesResponse getArticles(String tag, String author, String favorited, int limit, int page, String searchParam) {
        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        getArticlesResponse.setArticles(new ArrayList<>());
        List<ArticleEntity> articleEntities = articleRepository.findByCriteria(tag, author, favorited, limit, page, searchParam);

        Optional<SecurityUser> currentUser = authenticationService.getCurrentUser();
        Optional<UserEntity> currentUserEntity = Optional.empty();
        if (currentUser.isPresent()) {
            currentUserEntity = currentUser.filter(Objects::nonNull)
                    .map(SecurityUser::getUser).get();
        }

        for (ArticleEntity article: articleEntities) {
            GetArticlesSingleResponse articleResponse = new GetArticlesSingleResponse();
            articleResponse.setId(article.getId().toString());
            articleResponse.setBody(article.getBody());
            articleResponse.setDescription(article.getDescription());
            articleResponse.setSlug(article.getSlug());

            if(currentUserEntity.isPresent()) {
                UserEntity user = currentUserEntity.get();
                List<FavoriteEntity> favorites = article.getFavorites();
                articleResponse.setFavorited(favoriteUtils.isFavorited(user, favorites));
            }

            articleResponse.setFavoritesCount(article.getFavorites().size());
            List<ArticleTag> articleTagList = article.getArticleTags();
            List<String> tagList = new ArrayList<>();
            for (ArticleTag articleTag : articleTagList) {
                tagList.add(articleTag.getTag().getName());
            }
            articleResponse.setTagList(tagList);
            articleResponse.setTitle(article.getTitle());
            articleResponse.setCreatedAt(article.getCreateAt());
            articleResponse.setUpdatedAt(article.getUpdatedAt());

            GetArticlesAuthorResponse articleAuthorResponse = new GetArticlesAuthorResponse();
            UserEntity authorEntity = article.getAuthor();
            articleAuthorResponse.setUsername(authorEntity.getUsername());

            if(currentUserEntity.isPresent()) {
                articleAuthorResponse.setFollowing(followUtils.isFollowing(currentUserEntity.get(), authorEntity));
            }
            articleAuthorResponse.setFollowersCount(followUtils.getFollowingCount(authorEntity));
            articleAuthorResponse.setBio(authorEntity.getBio());
            articleAuthorResponse.setImage(authorEntity.getImage());

            articleResponse.setAuthor(articleAuthorResponse);
            getArticlesResponse.getArticles().add(articleResponse);

        }

        return getArticlesResponse;
    }

    public GetArticlesResponse getArticlesFeed(int size, int page, String searchParam) {
        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        getArticlesResponse.setArticles(new ArrayList<>());
        List<ArticleEntity> articleEntities = articleRepository.findByCriteria(null, null, null, size, page, searchParam);

        Optional<SecurityUser> currentUser = authenticationService.getCurrentUser();
        Optional<UserEntity> currentUserEntity = Optional.empty();
        if (currentUser.isPresent()) {
            currentUserEntity = currentUser.filter(Objects::nonNull)
                    .map(SecurityUser::getUser).get();
        }

        for (ArticleEntity article: articleEntities) {
            GetArticlesSingleResponse articleResponse = new GetArticlesSingleResponse();
            articleResponse.setId(article.getId().toString());
            articleResponse.setBody(article.getBody());
            articleResponse.setDescription(article.getDescription());
            articleResponse.setSlug(article.getSlug());

            if(currentUserEntity.isPresent()) {
                UserEntity user = currentUserEntity.get();
                List<FavoriteEntity> favorites = article.getFavorites();
                articleResponse.setFavorited(favoriteUtils.isFavorited(user, favorites));
            }

            articleResponse.setFavoritesCount(article.getFavorites().size());
            List<ArticleTag> articleTagList = article.getArticleTags();

            List<String> tagList = new ArrayList<>();
            for (ArticleTag articleTag : articleTagList) {
                tagList.add(articleTag.getTag().getName());
            }

            articleResponse.setTagList(tagList);
            articleResponse.setTitle(article.getTitle());
            articleResponse.setCreatedAt(article.getCreateAt());
            articleResponse.setUpdatedAt(article.getUpdatedAt());

            GetArticlesAuthorResponse articleAuthorResponse = new GetArticlesAuthorResponse();
            UserEntity authorEntity = article.getAuthor();
            articleAuthorResponse.setUsername(authorEntity.getUsername());

            if(currentUserEntity.isPresent()) {
                articleAuthorResponse.setFollowing(followUtils.isFollowing(currentUserEntity.get(), authorEntity));
            }
            articleAuthorResponse.setFollowersCount(followUtils.getFollowingCount(authorEntity));
            articleAuthorResponse.setBio(authorEntity.getBio());
            articleAuthorResponse.setImage(authorEntity.getImage());

            articleResponse.setAuthor(articleAuthorResponse);
            getArticlesResponse.getArticles().add(articleResponse);
        }

        return getArticlesResponse;
    }
}
