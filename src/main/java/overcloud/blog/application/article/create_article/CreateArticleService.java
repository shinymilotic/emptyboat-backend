package overcloud.blog.application.article.create_article;

import org.springframework.stereotype.Service;
import overcloud.blog.application.article.core.AuthorResponse;
import overcloud.blog.application.article.core.exception.InvalidDataException;
import overcloud.blog.application.article.core.repository.ArticleRepository;
import overcloud.blog.application.article.core.repository.ArticleTagRepository;
import overcloud.blog.application.article.core.utils.ArticleUtils;
import overcloud.blog.application.article_tag.core.ArticleTagId;
import overcloud.blog.application.tag.core.TagError;
import overcloud.blog.application.tag.core.repository.TagRepository;
import overcloud.blog.application.article_tag.core.ArticleTag;
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

    private final ArticleTagRepository articleTagRepository;

    public CreateArticleService(SpringAuthenticationService authenticationService,
                                TagRepository tagRepository,
                                ArticleRepository articleRepository,
                                ObjectsValidator<ArticleRequest> validator,
                                ArticleTagRepository articleTagRepository) {
        this.authenticationService = authenticationService;
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
        this.validator = validator;
        this.articleTagRepository = articleTagRepository;
    }

    public ArticleResponse createArticle(ArticleRequest articleRequest) {
        List<String> distinctTags = filterDistinctTags(articleRequest.getTagList());
        articleRequest.setTagList(distinctTags);
        Optional<ApiError> apiError = validator.validate(articleRequest);

        if(apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        List<TagEntity> tagEntities = tagRepository.findByTagName(articleRequest.getTagList());
        if(distinctTags.size() > tagEntities.size()) {
            throw new InvalidDataException(ApiError.from(TagError.TAG_NO_EXISTS));
        }

        ArticleEntity articleEntity = initArticleEntity(articleRequest, currentUser, tagEntities);
        ArticleEntity savedArticleEntity = articleRepository.save(articleEntity);
        List<ArticleTag> articleTags = toArticleTag(tagEntities, savedArticleEntity);
        articleTagRepository.saveAll(articleTags);

        return toCreateArticleResponse(savedArticleEntity);
    }

    private List<String> filterDistinctTags(List<String> tags) {
        if(tags != null) {
            tags = tags.stream().distinct().toList();
        } else {
            tags = new ArrayList<>();
        }

        return tags;
    }

    public ArticleEntity initArticleEntity(ArticleRequest articleRequest, UserEntity author, List<TagEntity> tagEntities) {
        String title = articleRequest.getTitle();
        String body = articleRequest.getBody();
        String description = articleRequest.getDescription();
        String slug = ArticleUtils.toSlug(title);
        LocalDateTime now = LocalDateTime.now();

        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setAuthor(author);
        articleEntity.setBody(body);
        articleEntity.setDescription(description);
        articleEntity.setSlug(slug);
        articleEntity.setTitle(title);
        articleEntity.setCreatedAt(now);
        articleEntity.setUpdatedAt(now);

        return articleEntity;
    }

    public List<ArticleTag> toArticleTag(List<TagEntity> tagEntities, ArticleEntity articleEntity) {
        return tagEntities.stream()
                .map(tagEntity -> {
                    ArticleTagId articleTagId = new ArticleTagId();
                    articleTagId.setArticleId(articleEntity.getId());
                    articleTagId.setTagId(tagEntity.getId());
                    return ArticleTag.builder()
                            .id(articleTagId)
                            .tag(tagEntity)
                            .article(articleEntity)
                            .build();})
                .collect(Collectors.toList());
    }

    public ArticleResponse toCreateArticleResponse(ArticleEntity articleEntity) {
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
