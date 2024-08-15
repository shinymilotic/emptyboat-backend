package overcloud.blog.usecase.blog.search;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.usecase.blog.common.ArticleResMsg;
import overcloud.blog.usecase.blog.common.ArticleSummary;
import overcloud.blog.usecase.blog.get_article_list.AuthorResponse;
import overcloud.blog.usecase.blog.get_article_list.GetArticlesResponse;
import overcloud.blog.usecase.blog.get_article_list.GetArticlesSingleResponse;
import overcloud.blog.usecase.common.auth.bean.SecurityUser;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArticleSearchServicePG implements ArticleSearchService {
    private final IArticleRepository articleRepository;
    private final SpringAuthenticationService authenticationService;
    private final ResFactory resFactory;

    public ArticleSearchServicePG(IArticleRepository articleRepository,
                                  SpringAuthenticationService authenticationService,
                                  ResFactory resFactory) {
        this.articleRepository = articleRepository;
        this.authenticationService = authenticationService;
        this.resFactory = resFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public RestResponse<GetArticlesResponse> searchArticles(String searchParam, int limit, String lastArticleId) {
        Optional<SecurityUser> currentSecurityUser = authenticationService.getCurrentUser();
        UserEntity currentUser = null;
        UUID currentUserId = null;
        if (currentSecurityUser.isPresent()) {
            currentUser = currentSecurityUser.get().getUser();
            currentUserId = currentUser.getUserId();
        }

        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        getArticlesResponse.setArticles(new ArrayList<>());
        List<ArticleSummary> articleSummaries = articleRepository.search(searchParam, currentUserId, limit, lastArticleId);

        for (ArticleSummary article : articleSummaries) {
            GetArticlesSingleResponse singleResponse = toGetArticlesSingleResponse(article);
            getArticlesResponse.getArticles().add(singleResponse);
            getArticlesResponse.addArticleCount();
        }

        return resFactory.success(ArticleResMsg.ARTICLE_GET_LIST, getArticlesResponse);
    }

    private GetArticlesSingleResponse toGetArticlesSingleResponse(ArticleSummary article) {
        return GetArticlesSingleResponse.builder()
                .id(article.getId().toString())
                .title(article.getTitle())
                .body(article.getBody())
                .description(article.getDescription())
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
