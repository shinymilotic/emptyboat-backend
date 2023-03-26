package overcloud.blog.application.article.api.service;

import jakarta.persistence.EntityNotFoundException;
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
import overcloud.blog.application.article.api.exception.ArticleError;
import overcloud.blog.application.article.api.repository.ArticleRepository;
import overcloud.blog.application.article.api.repository.ArticleTagRepository;
import overcloud.blog.application.article.comment.repository.CommentRepository;
import overcloud.blog.application.article.favorite.repository.FavoriteRepository;
import overcloud.blog.application.article.favorite.utils.FavoriteUtils;
import overcloud.blog.application.follow.repository.FollowRepository;
import overcloud.blog.application.follow.utils.FollowUtils;
import overcloud.blog.application.tag.repository.TagRepository;
import overcloud.blog.application.user.exception.login.LoginError;
import overcloud.blog.application.user.repository.UserRepository;
import overcloud.blog.domain.ArticleTag;
import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.article.tag.TagEntity;
import overcloud.blog.domain.user.UserEntity;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
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
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ArticleTagRepository articleTagRepository;


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


        for (TagEntity tagEntity : tagEntities) {
            if(tagList.contains(tagEntity.getName())) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticle(articleEntity);
                articleTag.setTag(tagEntity);
                tagForInsert.add(articleTag);
            }
        }
        articleEntity.setAuthor(securityUser);
        articleEntity.setBody(body);
        articleEntity.setDescription(description);
        articleEntity.setSlug(slug);
        articleEntity.setTitle(title);
        articleEntity.setCreateAt(now);
        articleEntity.setUpdatedAt(now);
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

    @Transactional
    public UpdateArticleResponse updateArticle(UpdateArticleRequest updateArticleRequest) {
        UpdateArticleResponse response = new UpdateArticleResponse();
        String id = updateArticleRequest.getId();
        String title = updateArticleRequest.getTitle();
        String body = updateArticleRequest.getBody();
        String description = updateArticleRequest.getDescription();
        String slug = URLConverter.toSlug(title);
        Set<String> tagList = new HashSet<>();
        updateArticleRequest.getTagList()
                .forEach(tagList::add);
        LocalDateTime now = LocalDateTime.now();

        UserEntity securityUser = authenticationService.getCurrentUser().get().getUser().get();

        ArticleEntity articleEntity = articleRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(ArticleError.ARTICLE_NOT_FOUND.getValue()));


        articleEntity.setTitle(title);
        articleEntity.setBody(body);
        articleEntity.setUpdatedAt(now);

        List<ArticleTag> articleTags = articleEntity.getArticleTags();

        deleteArticleTags(articleTags);

        List<ArticleTag> updateArticleTags = new ArrayList<>();
        for (String tag : tagList) {
            ArticleTag articleTag = new ArticleTag();
            TagEntity tagEntity = tagRepository.findByTagName(tag);
            articleTag.setArticle(articleEntity);
            articleTag.setTag(tagEntity);
            updateArticleTags.add(articleTag);
            articleTagRepository.save(articleTag);
        }

        articleEntity.setArticleTags(updateArticleTags);
        articleEntity.setSlug(slug);

        articleRepository.save(articleEntity);

        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setBio(securityUser.getBio());
        authorResponse.setUsername(securityUser.getUsername());
        authorResponse.setImage(securityUser.getImage());

        response.setId(id);
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

    private void deleteArticleTags(List<ArticleTag> articleTags) {
        for (ArticleTag articleTag : articleTags) {
            articleTagRepository.deleteArticleTags(articleTag.getArticle().getId(), articleTag.getTag().getId());
        }
    }

    @Transactional
    public DeleteArticleResponse deleteArticle(String id) {
        UUID uuid = UUID.fromString(id);
        DeleteArticleResponse deleteArticleResponse = new DeleteArticleResponse();
        commentRepository.deleteByArticle(uuid);
        favoriteRepository.deleteByArticle(uuid);
        articleRepository.deleteById(uuid);
        deleteArticleResponse.setId(uuid.toString());
        return deleteArticleResponse;
    }

    public GetArticlesResponse getArticles(String tag, String author, String favorited, int limit, int page) {
        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        getArticlesResponse.setArticles(new ArrayList<>());
        List<ArticleEntity> articleEntities = articleRepository.findByCriteria(tag, author, favorited, limit, page);
        Optional<UserEntity> securityUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .map(Optional::get);

        for (ArticleEntity article: articleEntities) {
            ArticleResponse articleResponse = new ArticleResponse();
            articleResponse.setId(article.getId().toString());
            articleResponse.setBody(article.getBody());
            articleResponse.setDescription(article.getDescription());
            articleResponse.setSlug(article.getSlug());
            if(securityUser.isPresent()) {
                articleResponse.setFavorited(favoriteUtils.isFavorited(securityUser.get(), article));
            }
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
            if(securityUser.isPresent()) {
                articleAuthorResponse.setFollowing(followUtils.isFollowing(securityUser.get(), authorEntity));
                articleAuthorResponse.setFollowersCount(followUtils.getFollowingCount(authorEntity));
            }
            articleAuthorResponse.setBio(authorEntity.getBio());
            articleAuthorResponse.setImage(authorEntity.getImage());

            articleResponse.setAuthor(articleAuthorResponse);
            getArticlesResponse.getArticles().add(articleResponse);

        }

        return getArticlesResponse;
    }

    public GetArticlesResponse getArticle(String slug) {
        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        ArticleEntity articleEntity = articleRepository.findBySlug(slug).get(0);
        UserEntity securityUser = authenticationService.getCurrentUser().get().getUser().get();


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
        articleAuthorResponse.setFollowersCount(followUtils.getFollowingCount(authorEntity));
        articleAuthorResponse.setBio(authorEntity.getBio());
        articleAuthorResponse.setImage(authorEntity.getImage());

        articleResponse.setAuthor(articleAuthorResponse);
        getArticlesResponse.setArticles(List.of(articleResponse));

        return getArticlesResponse;
    }

    public GetArticlesResponse getArticlesFeed(int size, int page) {
        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        getArticlesResponse.setArticles(new ArrayList<>());
        List<ArticleEntity> articleEntities = articleRepository.findByCriteria(null, null, null, size, page);
        Optional<UserEntity> securityUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .map(Optional::get);

        for (ArticleEntity article: articleEntities) {
            ArticleResponse articleResponse = new ArticleResponse();
            articleResponse.setId(article.getId().toString());
            articleResponse.setBody(article.getBody());
            articleResponse.setDescription(article.getDescription());
            articleResponse.setSlug(article.getSlug());
            if(securityUser.isPresent()) {
                articleResponse.setFavorited(favoriteUtils.isFavorited(securityUser.get(), article));
            }
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
            if(securityUser.isPresent()) {
                articleAuthorResponse.setFollowing(followUtils.isFollowing(securityUser.get(), authorEntity));
                articleAuthorResponse.setFollowersCount(followUtils.getFollowingCount(authorEntity));
            }
            articleAuthorResponse.setBio(authorEntity.getBio());
            articleAuthorResponse.setImage(authorEntity.getImage());

            articleResponse.setAuthor(articleAuthorResponse);
            getArticlesResponse.getArticles().add(articleResponse);

        }

        return getArticlesResponse;
    }


}
