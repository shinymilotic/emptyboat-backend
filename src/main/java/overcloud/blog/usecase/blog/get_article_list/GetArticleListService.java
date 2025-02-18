package overcloud.blog.usecase.blog.get_article_list;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.StringUtils;
import overcloud.blog.auth.bean.SecurityUser;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.usecase.blog.common.ArticleSummary;
import overcloud.blog.utils.validation.ObjectsValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetArticleListService {
    private final IArticleRepository articleRepository;
    private final SpringAuthenticationService authenticationService;
    private final ObjectsValidator validator;

    public GetArticleListService(IArticleRepository articleRepository,
                                 SpringAuthenticationService authenticationService,
                                 ObjectsValidator validator) {
        this.articleRepository = articleRepository;
        this.authenticationService = authenticationService;
        this.validator = validator;
    }

    @Transactional(readOnly = true)
    public GetArticlesResponse getArticles(String tag, String author, String favorited, int limit, String lastArticleId) {
        Optional<SecurityUser> currentSecurityUser = authenticationService.getCurrentUser();
        UUID tagId = null;
        if (StringUtils.hasText(tag)) {
            tagId = UUID.fromString(tag);
        }

        UserEntity currentUser = null;
        UUID currentUserId = null;
        if (currentSecurityUser.isPresent()) {
            currentUser = currentSecurityUser.get().getUser();
            currentUserId = currentUser.getUserId();
        }
        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        getArticlesResponse.setArticles(new ArrayList<>());
        List<ArticleSummary> articleSummaries = articleRepository.findBy(currentUserId, tagId, author, favorited, limit, lastArticleId);

        for (ArticleSummary article : articleSummaries) {
            GetArticlesSingleResponse singleResponse = toGetArticlesSingleResponse(article);
            getArticlesResponse.getArticles().add(singleResponse);
            getArticlesResponse.addArticleCount();
        }

        return getArticlesResponse;
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
