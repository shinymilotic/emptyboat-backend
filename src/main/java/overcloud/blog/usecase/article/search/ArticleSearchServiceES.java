package overcloud.blog.usecase.article.search;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.stereotype.Service;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.repository.jparepository.JpaArticleRepository;
import overcloud.blog.usecase.article.favorite.core.utils.FavoriteUtils;
import overcloud.blog.usecase.article.get_article_list.ArticleSummary;
import overcloud.blog.usecase.article.get_article_list.AuthorResponse;
import overcloud.blog.usecase.article.get_article_list.GetArticlesResponse;
import overcloud.blog.usecase.article.get_article_list.GetArticlesSingleResponse;
import overcloud.blog.usecase.user.follow.core.utils.FollowUtils;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArticleSearchServiceES implements ArticleSearchService{

    private final ElasticsearchOperations elasticsearchOperations;

    private final JpaArticleRepository articleRepository;

    private final SpringAuthenticationService authenticationService;


    public ArticleSearchServiceES(ElasticsearchOperations elasticsearchOperations,
                                  JpaArticleRepository articleRepository,
                                  SpringAuthenticationService authenticationService) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.articleRepository = articleRepository;
        this.authenticationService = authenticationService;
    }

    public GetArticlesResponse searchArticles(String searchParam, int size, String lastArticleId) {
        NativeQuery matchQuery = NativeQuery.builder()
                .withQuery(
                    query -> query.match(
                        mPP -> mPP.field("body").query(searchParam)
                    ))
                .withFilter( f ->
                                f.range(r -> r
                                        .field("id")
                                        .from(!lastArticleId.equals("") ? lastArticleId : null)
                                )
                )
                .withPageable(Pageable.ofSize(size))
                .build();

        SearchHits<ArticleElastic> searchHitsResult = elasticsearchOperations.search(matchQuery, ArticleElastic.class);
        List<UUID> articleElasticList = new ArrayList<>();
        if(searchHitsResult.hasSearchHits()) {
            articleElasticList = searchHitsResult.stream()
                    .map(SearchHit::getContent)
                    .map(content -> UUID.fromString(content.getId()))
                    .toList();
        }
        Optional<SecurityUser> currentSecurityUser = authenticationService.getCurrentUser();
        UUID currentUserId = null;
        UserEntity currentUser = null;
        if (currentSecurityUser.isPresent()) {
            currentUser = currentSecurityUser.get().getUser();
            currentUserId = currentUser.getId();
        }

        List<ArticleSummary> articleSummaries = articleRepository.findByIds(articleElasticList, currentUserId, lastArticleId);
        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        getArticlesResponse.setArticles(new ArrayList<>());
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
