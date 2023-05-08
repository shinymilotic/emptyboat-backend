package overcloud.blog.application.article.create_article;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import overcloud.blog.application.article.core.AuthorResponse;
import overcloud.blog.application.article.core.exception.WriteArticleException;
import overcloud.blog.application.article.core.repository.ArticleRepository;
import overcloud.blog.application.tag.core.repository.TagRepository;
import overcloud.blog.application.article_tag.ArticleTag;
import overcloud.blog.application.article.core.ArticleEntity;
import overcloud.blog.application.tag.core.TagEntity;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.ApiValidationError;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.string.URLConverter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CreateArticleService {

    private final SpringAuthenticationService authenticationService;

    private final TagRepository tagRepository;

    private final ArticleRepository articleRepository;

    public CreateArticleService(SpringAuthenticationService authenticationService,
                                TagRepository tagRepository,
                                ArticleRepository articleRepository) {
        this.authenticationService = authenticationService;
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
    }

    public Optional<ApiError> validate(CreateArticleRequest request) {
        ApiError apiError = ApiError.from("Validation error");

        List<ApiValidationError> apiValidationError = validateCreateArticleDto(request);
        for (ApiValidationError error : apiValidationError) {
            apiError.addApiValidationErrorDetail(error);
        }

        validateTagList(request.getTagList())
                .ifPresent(apiError::addApiValidationErrorDetail);

        if(!StringUtils.hasText(request.getTitle())) {
            apiError.addApiValidationErrorDetail(ApiValidationError.addValidationError(
                    "CreateArticle",
                    "title",
                    "",
                    "title is required"));
        } else {
            validateTitle(request.getTitle())
                    .ifPresent(apiError::addApiValidationErrorDetail);
        }

        if(apiError.getApiErrorDetails().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(apiError);
    }

    private List<ApiValidationError> validateCreateArticleDto(CreateArticleRequest request) {
        List<ApiValidationError> apiValidationErrors = new ArrayList<>();

        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            final Validator validator = validatorFactory.getValidator();
            final Set<ConstraintViolation<CreateArticleRequest>> constraintsViolations = validator.validate(request);

            if (!constraintsViolations.isEmpty()) {
                Iterator<ConstraintViolation<CreateArticleRequest>> cvIterator = constraintsViolations.iterator();
                while(cvIterator.hasNext()) {
                    ConstraintViolation<?> cv = cvIterator.next();
                    apiValidationErrors.add(ApiValidationError.addValidationError(
                            cv.getRootBeanClass().getSimpleName(),
                            cv.getPropertyPath().toString(),
                            cv.getInvalidValue(),
                            cv.getMessage()));
                }

            }
        }

        return apiValidationErrors;
    }

    private Optional<ApiValidationError> validateTagList(Collection<String> tagList) {

        if(tagList == null || tagList.isEmpty()) {
            return Optional.empty();
        }

        List<TagEntity> tagEntities = tagRepository.checkAllTagsExistDB(tagList);

        if(tagEntities.size() != tagList.size()) {
            return Optional.of(ApiValidationError.addValidationError(
                    "CreateArticle",
                    "tagList",
                    "",
                    "There is tag doesn't exist"));
        }

        return Optional.empty();
    }

    private Optional<ApiValidationError> validateTitle(String title) {
        List<ArticleEntity> articleEntities = articleRepository.findByTitle(title);

        if(!articleEntities.isEmpty()) {
            return Optional.of(ApiValidationError.addValidationError(
                    "CreateArticle",
                    "title",
                    "",
                    "Title existed"));
        }

        return Optional.empty();
    }

    public CreateArticleResponse createArticle(CreateArticleRequest articleRequest) {
        String title = articleRequest.getTitle();
        String body = articleRequest.getBody();
        String description = articleRequest.getDescription();
        String slug = URLConverter.toSlug(title);
        LocalDateTime now = LocalDateTime.now();

        Optional<ApiError> apiError = validate(articleRequest);

        if(apiError.isPresent()) {
            throw new WriteArticleException(apiError.get());
        }

        UserEntity currentUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .orElseThrow(EntityNotFoundException::new)
                .orElseThrow(EntityNotFoundException::new);

        ArticleEntity articleEntity = new ArticleEntity();
        List<TagEntity> tagEntities = tagRepository.findByTagName(articleRequest.getTagList());
        List<ArticleTag> articleTags = tagEntities.stream()
                                .map(tagEntity -> ArticleTag.builder()
                                .tag(tagEntity)
                                .article(articleEntity)
                                .build()).collect(Collectors.toList());

        articleEntity.setAuthor(currentUser);
        articleEntity.setBody(body);
        articleEntity.setDescription(description);
        articleEntity.setSlug(slug);
        articleEntity.setTitle(title);
        articleEntity.setCreateAt(now);
        articleEntity.setUpdatedAt(now);
        articleEntity.setArticleTags(articleTags);
        articleRepository.save(articleEntity);

        return toCreateArticleResponse(articleEntity);
    }

    private CreateArticleResponse toCreateArticleResponse(ArticleEntity articleEntity) {
        return CreateArticleResponse.builder()
                .title(articleEntity.getTitle())
                .body(articleEntity.getBody())
                .description(articleEntity.getDescription())
                .tagList(articleEntity.getTagNameList())
                .author(toAuthorResponse(articleEntity.getAuthor()))
                .slug(articleEntity.getSlug())
                .createdAt(articleEntity.getCreateAt())
                .updatedAt(articleEntity.getUpdatedAt())
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
