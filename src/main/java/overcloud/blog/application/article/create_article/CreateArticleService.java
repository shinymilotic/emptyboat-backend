package overcloud.blog.application.article.create_article;

import org.springframework.stereotype.Service;
import overcloud.blog.application.article.core.AuthorResponse;
import overcloud.blog.application.article.core.exception.InvalidDataException;
import overcloud.blog.application.article.core.repository.ArticleRepository;
import overcloud.blog.application.article.core.utils.ArticleUtils;
import overcloud.blog.application.article.article_tag.ArticleTagId;
import overcloud.blog.application.tag.core.repository.TagRepository;
import overcloud.blog.application.article.article_tag.ArticleTag;
import overcloud.blog.application.article.core.ArticleEntity;
import overcloud.blog.application.tag.core.TagEntity;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserError;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.validation.ObjectsValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CreateArticleService {

    private final SpringAuthenticationService authenticationService;

    private final TagRepository tagRepository;

    private final ArticleRepository articleRepository;

    private final ObjectsValidator<ArticleRequest> validator;

    public CreateArticleService(SpringAuthenticationService authenticationService,
                                TagRepository tagRepository,
                                ArticleRepository articleRepository,
                                ObjectsValidator<ArticleRequest> validator) {
        this.authenticationService = authenticationService;
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
        this.validator = validator;
    }

    public ArticleResponse createArticle(ArticleRequest articleRequest) {
        Optional<ApiError> apiError = validator.validate(articleRequest);
        if(!apiError.isEmpty()) {
            throw new InvalidDataException(apiError.get());
        }

        String title = articleRequest.getTitle();
        String body = articleRequest.getBody();
        String description = articleRequest.getDescription();
        String slug = ArticleUtils.toSlug(title);
        LocalDateTime now = LocalDateTime.now();

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        ArticleEntity articleEntity = new ArticleEntity();
        List<TagEntity> tagEntities = tagRepository.findByTagName(articleRequest.getTagList());
        List<ArticleTag> articleTags = tagEntities.stream()
                                .map(tagEntity -> ArticleTag.builder()
                                .id(new ArticleTagId())
                                .tag(tagEntity)
                                .article(articleEntity)
                                .build()).collect(Collectors.toList());

        articleEntity.setAuthor(currentUser);
        articleEntity.setBody(body);
        articleEntity.setDescription(description);
        articleEntity.setSlug(slug);
        articleEntity.setTitle(title);
        articleEntity.setCreatedAt(now);
        articleEntity.setUpdatedAt(now);
        articleEntity.setArticleTags(articleTags);
        articleRepository.save(articleEntity);

        return toCreateArticleResponse(articleEntity);
    }

    private ArticleResponse toCreateArticleResponse(ArticleEntity articleEntity) {
        return ArticleResponse.builder()
                .id(articleEntity.getId().toString())
                .title(articleEntity.getTitle())
                .body(articleEntity.getBody())
                .description(articleEntity.getDescription())
                .tagList(articleEntity.getTagNameList())
                .author(toAuthorResponse(articleEntity.getAuthor()))
                .slug(articleEntity.getSlug())
                .createdAt(articleEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm")))
                .updatedAt(articleEntity.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm")))
                .favorited(false)
                .build();
    }

    private AuthorResponse toAuthorResponse(UserEntity userEntity) {
        return AuthorResponse.builder()
              .bio(userEntity.getBio())
              .username(userEntity.getUsername())
              .image(userEntity.getImage())
              .build();
    }
}
