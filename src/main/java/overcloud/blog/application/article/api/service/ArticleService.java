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
        articleRepository.save(articleEntity);

        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setBio(securityUser.getBio());
        authorResponse.setUsername(securityUser.getUsername());
        authorResponse.setImage(securityUser.getImage());

        response.setAuthorResponse(authorResponse);
        response.setTagList(tagList);
        response.setBody(body);
        response.setFavorited(false);
        response.setDescription(description);
        response.setSlug(slug);
        response.setTitle(title);
        response.setFavoritesCount(0);
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
        List<ArticleEntity> articleEntities = articleRepository.findByTagAndAuthorAndFavorite(tag, author, favorited, limit, offset);

        for (ArticleEntity article: articleEntities) {
            ArticleResponse articleResponse = new ArticleResponse();
            articleResponse.setId(article.getId().toString());
            articleResponse.setBody(article.getBody());
            articleResponse.setDescription(article.getDescription());
            articleResponse.setSlug(article.getSlug());
            articleResponse.setFavorited(false);
            articleResponse.setFavoritesCount(article.getFavorites().size());
            articleResponse.setTagList(null);
            articleResponse.setTitle(null);
            articleResponse.setCreatedAt(null);
            articleResponse.setUpdatedAt(null);

            GetArticleAuthorResponse articleAuthorResponse = new GetArticleAuthorResponse();
            UserEntity authorEntity = article.getAuthor();
            articleAuthorResponse.setUsername(authorEntity.getUsername());
            articleAuthorResponse.setFollowing(false);
            articleAuthorResponse.setBio(authorEntity.getBio());
            articleAuthorResponse.setImage(authorEntity.getImage());

            articleResponse.setAuthor(articleAuthorResponse);
            getArticlesResponse.getArticles().add(articleResponse);

        }

        return getArticlesResponse;
    }
}
