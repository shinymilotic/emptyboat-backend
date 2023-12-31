package overcloud.blog.usecase.article.search;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.stereotype.Service;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.repository.jparepository.JpaArticleRepository;
import overcloud.blog.usecase.article.favorite.core.utils.FavoriteUtils;
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
public class ArticleSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    private final JpaArticleRepository articleRepository;

    private final SpringAuthenticationService authenticationService;

    private final FollowUtils followUtils;

    private final FavoriteUtils favoriteUtils;

    public ArticleSearchService(ElasticsearchOperations elasticsearchOperations,
                                JpaArticleRepository articleRepository,
                                SpringAuthenticationService authenticationService,
                                FollowUtils followUtils,
                                FavoriteUtils favoriteUtils) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.articleRepository = articleRepository;
        this.authenticationService = authenticationService;
        this.followUtils = followUtils;
        this.favoriteUtils = favoriteUtils;
    }

    public GetArticlesResponse searchArticles(String searchParam, int size, int page) {
        NativeQuery matchQuery = NativeQuery.builder()
                .withQuery(
                    query -> query.match(
                        mPP -> mPP.field("body").query(searchParam)
                    ))
                .withPageable(PageRequest.of(page - 1, size))
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
        UserEntity currentUser = null;
        if (currentSecurityUser.isPresent()) {
            currentUser = currentSecurityUser.get().getUser();
        }

        List<ArticleEntity> articleEntities = articleRepository.findAllById(articleElasticList);
        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        getArticlesResponse.setArticles(new ArrayList<>());
        for (ArticleEntity article: articleEntities) {
            GetArticlesSingleResponse singleResponse = toGetArticlesSingleResponse(article, currentUser);
            getArticlesResponse.getArticles().add(singleResponse);
        }
        return getArticlesResponse;
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
//                .favoritesCount(article.getFavorites().size())
                .tagList(article.getTagNameList())
                .createdAt(article.getCreatedAt())
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
