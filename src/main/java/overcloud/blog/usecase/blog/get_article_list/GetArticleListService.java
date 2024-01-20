package overcloud.blog.usecase.blog.get_article_list;

import org.springframework.stereotype.Service;
import overcloud.blog.repository.jparepository.JpaArticleRepository;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetArticleListService {


    private final JpaArticleRepository articleRepository;

    private final SpringAuthenticationService authenticationService;

    public GetArticleListService(JpaArticleRepository articleRepository,
                                 SpringAuthenticationService authenticationService) {
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
        List<ArticleSummary> articleSummaries = articleRepository.findByCriteria(currentUserId, tag, author, favorited, limit, lastArticleId);

        for (ArticleSummary article : articleSummaries) {
            GetArticlesSingleResponse singleResponse = toGetArticlesSingleResponse(article, currentUser);
            getArticlesResponse.getArticles().add(singleResponse);
            getArticlesResponse.addArticleCount();
        }

        return getArticlesResponse;
    }


    private GetArticlesSingleResponse toGetArticlesSingleResponse(ArticleSummary article, UserEntity currentUser) {
        return GetArticlesSingleResponse.builder()
                .id(article.getId().toString())
                .title(article.getTitle())
                .body(article.getBody())
                .description(article.getDescription())
                .slug(article.getSlug())
                .author(toGetArticleAuthorResponse(currentUser, article))
                .favorited(article.isFavorited())
                .favoritesCount(article.getFavoritesCount())
                .tagList(article.getTag())
                .createdAt(article.getCreatedAt().toLocalDateTime())
                .build();
    }

    private AuthorResponse toGetArticleAuthorResponse(UserEntity currentUser, ArticleSummary author) {
        return AuthorResponse.builder()
                .username(author.getUsername())
                .bio(author.getBio())
                .image(author.getImage())
                .following(author.getFollowing())
                .followersCount(author.getFollowersCount())
                .build();
    }
}
