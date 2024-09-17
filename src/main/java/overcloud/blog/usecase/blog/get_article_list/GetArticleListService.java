package overcloud.blog.usecase.blog.get_article_list;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.common.auth.bean.SecurityUser;
import overcloud.blog.common.auth.service.SpringAuthenticationService;
import overcloud.blog.common.response.ResFactory;
import overcloud.blog.common.response.RestResponse;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.usecase.blog.common.ArticleResMsg;
import overcloud.blog.usecase.blog.common.ArticleSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetArticleListService {
    private final IArticleRepository articleRepository;
    private final SpringAuthenticationService authenticationService;
    private final ResFactory resFactory;

    public GetArticleListService(IArticleRepository articleRepository,
                                 SpringAuthenticationService authenticationService,
                                 ResFactory resFactory) {
        this.articleRepository = articleRepository;
        this.authenticationService = authenticationService;
        this.resFactory = resFactory;
    }

    @Transactional(readOnly = true)
    public RestResponse<GetArticlesResponse> getArticles(String tag, String author, String favorited, int limit, String lastArticleId) {
        Optional<SecurityUser> currentSecurityUser = authenticationService.getCurrentUser();
        UserEntity currentUser = null;
        UUID currentUserId = null;
        if (currentSecurityUser.isPresent()) {
            currentUser = currentSecurityUser.get().getUser();
            currentUserId = currentUser.getUserId();
        }
        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        getArticlesResponse.setArticles(new ArrayList<>());
        List<ArticleSummary> articleSummaries = articleRepository.findBy(currentUserId, tag, author, favorited, limit, lastArticleId);

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
