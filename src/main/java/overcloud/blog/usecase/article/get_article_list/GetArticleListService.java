package overcloud.blog.usecase.article.get_article_list;

import org.springframework.stereotype.Service;
import overcloud.blog.repository.jparepository.JpaArticleRepository;
import overcloud.blog.usecase.article.favorite.core.utils.FavoriteUtils;
import overcloud.blog.usecase.user.follow.core.utils.FollowUtils;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetArticleListService {

    private final FollowUtils followUtils;

    private final FavoriteUtils favoriteUtils;

    private final JpaArticleRepository articleRepository;

    private final SpringAuthenticationService authenticationService;

    public GetArticleListService(FollowUtils followUtils,
                                 FavoriteUtils favoriteUtils,
                                 JpaArticleRepository articleRepository,
                                 SpringAuthenticationService authenticationService) {
        this.followUtils = followUtils;
        this.favoriteUtils = favoriteUtils;
        this.articleRepository = articleRepository;
        this.authenticationService = authenticationService;
    }

    public GetArticlesResponse getArticles(String tag, String author, String favorited, int limit, String lastArticleId) {
        Optional<SecurityUser> currentSecurityUser = authenticationService.getCurrentUser();
        UserEntity currentUser = null;
        UUID currentUserId = null;
        if (currentSecurityUser.isPresent()) {
            currentUser = currentSecurityUser.get().getUser();
            currentUserId = currentUser.getId();
        }

        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        getArticlesResponse.setArticles(new ArrayList<>());
        List<ArticleSummary> articleEntities = articleRepository.findByCriteria(currentUserId, tag, author, favorited, limit, lastArticleId);

//        Optional<SecurityUser> currentSecurityUser = authenticationService.getCurrentUser();
//        UserEntity currentUser = null;
//        if (currentSecurityUser.isPresent()) {
//            currentUser = currentSecurityUser.get().getUser();
//        }
//
//        for (ArticleEntity article: articleEntities) {
//            GetArticlesSingleResponse singleResponse = toGetArticlesSingleResponse(article, currentUser);
//            getArticlesResponse.getArticles().add(singleResponse);
//        }

        return null;
    }


    private GetArticlesSingleResponse toGetArticlesSingleResponse(ArticleEntity article, UserEntity currentUser) {
        return GetArticlesSingleResponse.builder()
                .id(article.getId().toString())
                .title(article.getTitle())
                .body(article.getBody())
                .description(article.getDescription())
                .slug(article.getSlug())
                .author(toGetArticleAuthorResponse(currentUser, article.getAuthor()))
                .favorited(favoriteUtils.isFavorited(currentUser, article))
                .favoritesCount(article.getFavorites().size())
                .tagList(article.getTagNameList())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    private AuthorResponse toGetArticleAuthorResponse(UserEntity currentUser, UserEntity author) {
        return AuthorResponse.builder()
                .username(author.getUsername())
                .bio(author.getBio())
                .image(author.getImage())
                .following(followUtils.isFollowing(currentUser, author))
                .followersCount(followUtils.getFollowingCount(author))
                .build();
    }
}
