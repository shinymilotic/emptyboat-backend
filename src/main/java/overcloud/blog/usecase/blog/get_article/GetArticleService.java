package overcloud.blog.usecase.blog.get_article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.usecase.blog.common.ArticleSummary;
import overcloud.blog.usecase.common.auth.bean.SecurityUser;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;

import java.util.UUID;

@Service
public class GetArticleService {

    private final IArticleRepository articleRepository;

    private final SpringAuthenticationService authenticationService;


    public GetArticleService(IArticleRepository articleRepository,
                             SpringAuthenticationService authenticationService) {
        this.articleRepository = articleRepository;
        this.authenticationService = authenticationService;
    }

    @Transactional(readOnly = true)
    public GetArticleResponse getArticle(String slug) {
        SecurityUser securityUser = authenticationService.getCurrentUser()
                .orElse(null);

        UserEntity currentUser = null;
        UUID articleId = null;
        if (securityUser != null && securityUser.getUser() != null) {
            currentUser = securityUser.getUser();
            articleId = currentUser.getId();
        }

        ArticleSummary articleSummary = articleRepository.findArticleBySlug(slug, articleId);

        return toGetArticlesingleResponse(articleSummary, currentUser);
    }

    private GetArticleResponse toGetArticlesingleResponse(ArticleSummary article, UserEntity currentUser) {
        return GetArticleResponse.builder()
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
