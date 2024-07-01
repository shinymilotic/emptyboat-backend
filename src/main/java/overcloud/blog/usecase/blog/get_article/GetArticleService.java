package overcloud.blog.usecase.blog.get_article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.usecase.blog.common.ArticleResMsg;
import overcloud.blog.usecase.blog.common.ArticleSummary;
import overcloud.blog.usecase.common.auth.bean.SecurityUser;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import java.util.UUID;

@Service
public class GetArticleService {
    private final IArticleRepository articleRepository;
    private final SpringAuthenticationService authenticationService;
    private final ResFactory resFactory;

    public GetArticleService(IArticleRepository articleRepository,
                             SpringAuthenticationService authenticationService,
                             ResFactory resFactory) {
        this.articleRepository = articleRepository;
        this.authenticationService = authenticationService;
        this.resFactory = resFactory;
    }

    @Transactional(readOnly = true)
    public RestResponse<GetArticleResponse> getArticle(String slug) {
        SecurityUser securityUser = authenticationService.getCurrentUser()
                .orElse(null);

        UserEntity currentUser = null;
        UUID articleId = null;
        if (securityUser != null && securityUser.getUser() != null) {
            currentUser = securityUser.getUser();
            articleId = currentUser.getId();
        }

        ArticleSummary articleSummary = articleRepository.findArticleBySlug(slug, articleId);

        return resFactory.success(ArticleResMsg.ARTICLE_GET_SUCCESS, toGetArticlesingleResponse(articleSummary));
    }

    private GetArticleResponse toGetArticlesingleResponse(ArticleSummary article) {
        return GetArticleResponse.builder()
                .id(article.getId().toString())
                .title(article.getTitle())
                .body(article.getBody())
                .description(article.getDescription())
                .slug(article.getSlug())
                .author(toGetArticleAuthorResponse(article))
                .favorited(article.isFavorited())
                .favoritesCount(article.getFavoritesCount())
                .tagList(article.getTag())
                .createdAt(article.getCreatedAt().toLocalDateTime())
                .build();
    }

    private AuthorResponse toGetArticleAuthorResponse(ArticleSummary author) {
        return AuthorResponse.builder()
                .username(author.getUsername())
                .bio(author.getBio())
                .image(author.getImage())
                .following(author.getFollowing())
                .followersCount(author.getFollowersCount())
                .build();
    }
}
