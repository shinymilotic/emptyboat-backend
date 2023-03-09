package overcloud.blog.application.article.api.service;

import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.application.article.api.dto.AuthorResponse;
import overcloud.blog.application.article.api.dto.create.CreateArticleRequest;
import overcloud.blog.application.article.api.dto.create.CreateArticleResponse;
import overcloud.blog.application.article.api.dto.delete.DeleteArticleResponse;
import overcloud.blog.application.article.api.dto.get.ArticleResponse;
import overcloud.blog.application.article.api.dto.get.GetArticleAuthorResponse;
import overcloud.blog.application.article.api.dto.get.GetArticlesResponse;
import overcloud.blog.application.article.api.dto.update.UpdateArticleRequest;
import overcloud.blog.application.article.api.dto.update.UpdateArticleResponse;
import overcloud.blog.application.article.api.repository.ArticleRepository;
import overcloud.blog.application.article.favorite.utils.FavoriteUtils;
import overcloud.blog.application.follow.utils.FollowUtils;
import overcloud.blog.application.tag.repository.TagRepository;
import overcloud.blog.application.user.repository.UserRepository;
import overcloud.blog.domain.ArticleTag;
import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.article.tag.TagEntity;
import overcloud.blog.domain.user.UserEntity;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.string.URLConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SpringAuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteUtils favoriteUtils;

    @Autowired
    private FollowUtils followUtils;

    public CreateArticleResponse createArticle(CreateArticleRequest articleRequest) {
        CreateArticleResponse response = new CreateArticleResponse();
        String title = articleRequest.getTitle();
        String body = articleRequest.getBody();
        String description = articleRequest.getDescription();
        String slug = URLConverter.toSlug(title);
        Set<String> tagList = new HashSet<>();
        articleRequest.getTagList()
                    .forEach(tagList::add);
        List<TagEntity> tagEntities = tagRepository.findAll();
        List<ArticleTag> tagForInsert = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        ArticleEntity articleEntity = new ArticleEntity();
        UserEntity securityUser = authenticationService.getCurrentUser().get().getUser().get();
        articleEntity.setAuthor(securityUser);
        articleEntity.setBody(body);
        articleEntity.setDescription(description);
        articleEntity.setSlug(slug);
        articleEntity.setTitle(title);
        articleEntity.setCreateAt(now);
        articleEntity.setUpdatedAt(now);

        for (TagEntity tagEntity : tagEntities) {
            if(tagList.contains(tagEntity.getName())) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticle(articleEntity);
                articleTag.setTag(tagEntity);
                tagForInsert.add(articleTag);
            }
        }

        articleEntity.setArticleTags(tagForInsert);
        ArticleEntity savedArticleEntity = articleRepository.save(articleEntity);

        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setBio(securityUser.getBio());
        authorResponse.setUsername(securityUser.getUsername());
        authorResponse.setImage(securityUser.getImage());

        response.setAuthorResponse(authorResponse);
        response.setTagList(tagList);
        response.setBody(body);
        response.setFavorited(favoriteUtils.isFavorited(securityUser, savedArticleEntity));
        response.setDescription(description);
        response.setSlug(slug);
        response.setTitle(title);
        response.setFavoritesCount(savedArticleEntity.getFavorites().size());
        response.setCreatedAt(now);
        response.setUpdatedAt(now);

        return response;
    }

    public UpdateArticleResponse updateArticle(UpdateArticleRequest updateArticleRequest) {
        UpdateArticleResponse updateArticleResponse = new UpdateArticleResponse();
        /*String updateBody = updateArticleRequest.getBody();
        updateArticleRequest.getDescription();
        updateArticleRequest.getTitle();
        updateArticleRequest.getTagList();

        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setTitle();
        articleEntity.setSlug();
        articleEntity.setBody();
        articleEntity.set*/

        /*articleRepository.save()*/
        return updateArticleResponse;
    }
    @Transactional
    public DeleteArticleResponse deleteArticle(String id) {
        UUID uuid = UUID.fromString(id);
        DeleteArticleResponse deleteArticleResponse = new DeleteArticleResponse();
        articleRepository.deleteById(uuid);
        deleteArticleResponse.setId(uuid.toString());
        return deleteArticleResponse;
    }

    public GetArticlesResponse getArticles(String tag, String author, String favorited, int limit, int offset) {
        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        getArticlesResponse.setArticles(new ArrayList<>());
        List<ArticleEntity> articleEntities = articleRepository.findByCriteria(tag, author, favorited, limit, offset);
        UserEntity securityUser = authenticationService.getCurrentUser().get().getUser().get();

        for (ArticleEntity article: articleEntities) {
            ArticleResponse articleResponse = new ArticleResponse();
            articleResponse.setId(article.getId().toString());
            articleResponse.setBody(article.getBody());
            articleResponse.setDescription(article.getDescription());
            articleResponse.setSlug(article.getSlug());
            articleResponse.setFavorited(favoriteUtils.isFavorited(securityUser, article));
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

            GetArticleAuthorResponse articleAuthorResponse = new GetArticleAuthorResponse();
            UserEntity authorEntity = article.getAuthor();
            articleAuthorResponse.setUsername(authorEntity.getUsername());
            articleAuthorResponse.setFollowing(followUtils.isFollowing(securityUser, authorEntity));
            articleAuthorResponse.setBio(authorEntity.getBio());
            articleAuthorResponse.setImage(authorEntity.getImage());

            articleResponse.setAuthor(articleAuthorResponse);
            getArticlesResponse.getArticles().add(articleResponse);

        }

        return getArticlesResponse;
    }

    public GetArticlesResponse getArticle(String slug) {
        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        List<ArticleEntity> articleList = articleRepository.findBySlug(slug);
        ArticleEntity articleEntity = new ArticleEntity();
        UserEntity securityUser = authenticationService.getCurrentUser().get().getUser().get();

        if(!articleList.isEmpty()) {
            articleEntity = articleList.get(0);
        }
        ArticleResponse articleResponse = new ArticleResponse();
        articleResponse.setId(articleEntity.getId().toString());
        articleResponse.setBody(articleEntity.getBody());
        articleResponse.setDescription(articleEntity.getDescription());
        articleResponse.setSlug(articleEntity.getSlug());
        articleResponse.setFavorited(favoriteUtils.isFavorited(securityUser, articleEntity));
        articleResponse.setFavoritesCount(articleEntity.getFavorites().size());
        articleResponse.setTitle(articleEntity.getTitle());
        articleResponse.setCreatedAt(articleEntity.getCreateAt());
        articleResponse.setUpdatedAt(articleEntity.getUpdatedAt());

        List<ArticleTag> articleTagList = articleEntity.getArticleTags();
        List<String> tagList = new ArrayList<>();
        for (ArticleTag articleTag : articleTagList) {
            tagList.add(articleTag.getTag().getName());
        }
        articleResponse.setTagList(tagList);

        GetArticleAuthorResponse articleAuthorResponse = new GetArticleAuthorResponse();
        UserEntity authorEntity = articleEntity.getAuthor();
        articleAuthorResponse.setUsername(authorEntity.getUsername());
        articleAuthorResponse.setFollowing(followUtils.isFollowing(securityUser, authorEntity));
        articleAuthorResponse.setBio(authorEntity.getBio());
        articleAuthorResponse.setImage(authorEntity.getImage());

        articleResponse.setAuthor(articleAuthorResponse);
        getArticlesResponse.setArticles(List.of(articleResponse));

        return getArticlesResponse;
    }
}
