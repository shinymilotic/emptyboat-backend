package overcloud.blog.application.article.update_article;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.application.article.core.AuthorResponse;
import overcloud.blog.application.article.core.exception.ArticleError;
import overcloud.blog.application.article.core.exception.WriteArticleException;
import overcloud.blog.application.article.core.repository.ArticleRepository;
import overcloud.blog.application.article.core.repository.ArticleTagRepository;
import overcloud.blog.application.tag.core.repository.TagRepository;
import overcloud.blog.application.article_tag.ArticleTag;
import overcloud.blog.application.article.core.ArticleEntity;
import overcloud.blog.application.tag.core.TagEntity;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.ApiValidationError;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.string.URLConverter;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UpdateArticleService {

    private final ArticleRepository articleRepository;

    private final TagRepository tagRepository;

    private final ArticleTagRepository articleTagRepository;

    public UpdateArticleService(ArticleRepository articleRepository,
                                TagRepository tagRepository,
                                ArticleTagRepository articleTagRepository) {
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
        this.articleTagRepository = articleTagRepository;
    }

    @Transactional
    public UpdateArticleResponse updateArticle(UpdateArticleRequest updateArticleRequest, String currentSlug) {
        String id = updateArticleRequest.getId();
        String title = updateArticleRequest.getTitle();
        String body = updateArticleRequest.getBody();
        String description = updateArticleRequest.getDescription();
        String slug = URLConverter.toSlug(title);
        LocalDateTime now = LocalDateTime.now();
        Optional<ApiError> apiError = validate(updateArticleRequest, currentSlug, slug);

        if(apiError.isPresent()) {
            throw new WriteArticleException(apiError.get());
        }

        ArticleEntity articleEntity = articleRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(ArticleError.ARTICLE_NOT_FOUND.getValue()));

        articleEntity.setTitle(title);
        articleEntity.setDescription(description);
        articleEntity.setBody(body);
        articleEntity.setUpdatedAt(now);
        List<ArticleTag> articleTags = articleEntity.getArticleTags();
        deleteArticleTags(articleTags);
        List<TagEntity> tagEntities = tagRepository.findByTagName(updateArticleRequest.getTagList());
        List<ArticleTag> updateArticleTags = tagEntities.stream()
                                        .map(tagEntity -> ArticleTag.builder()
                                            .article(articleEntity)
                                            .tag(tagEntity).build())
                                        .map(articleTagRepository::save).toList();
        articleEntity.setArticleTags(updateArticleTags);
        articleEntity.setSlug(slug);

        articleRepository.save(articleEntity);

        return toUpdateArticleResponse(articleEntity);
    }

    private UpdateArticleResponse toUpdateArticleResponse(ArticleEntity articleEntity) {
        return UpdateArticleResponse.builder()
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

    private void deleteArticleTags(List<ArticleTag> articleTags) {
        for (ArticleTag articleTag : articleTags) {
            articleTagRepository.deleteArticleTags(articleTag.getArticle().getId(), articleTag.getTag().getId());
        }
    }

    public Optional<ApiError> validate(UpdateArticleRequest request, String currentSlug, String newSlug) {
        ApiError apiError = ApiError.from("Validation error");

        List<ApiValidationError> apiValidationError = validateUpdateArticleDto(request);
        for (ApiValidationError error : apiValidationError) {
            apiError.addApiValidationErrorDetail(error);
        }

        Optional<ApiValidationError> tagError = validateTagList(request.getTagList());
        Optional<ApiValidationError> slugError = validateSlug(currentSlug, newSlug);

        if(tagError.isPresent()) {
            apiError.addApiValidationErrorDetail(tagError.get());
        }

        if(slugError.isPresent()) {
            apiError.addApiValidationErrorDetail(slugError.get());
        }

        if(apiError.getApiErrorDetails().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(apiError);
    }

    private List<ApiValidationError> validateUpdateArticleDto(UpdateArticleRequest request) {
        List<ApiValidationError> apiValidationErrors = new ArrayList<>();

        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            final Validator validator = validatorFactory.getValidator();
            final Set<ConstraintViolation<UpdateArticleRequest>> constraintsViolations = validator.validate(request);

            if (!constraintsViolations.isEmpty()) {
                Iterator<ConstraintViolation<UpdateArticleRequest>> cvIterator = constraintsViolations.iterator();
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

        if(tagEntities.size() != 0 && tagEntities.size() != tagList.size()) {
            return Optional.of(ApiValidationError.addValidationError(
                    "UpdateArticle",
                    "tagList",
                    "",
                    "tag list doesn't exist"));
        }

        return Optional.empty();
    }

    private Optional<ApiValidationError> validateSlug(String currentSlug, String newSlug) {
        List<ArticleEntity> articleEntities = articleRepository.findBySlug(newSlug);

        if(!currentSlug.equals(newSlug) && !articleEntities.isEmpty()) {
            return Optional.of(ApiValidationError.addValidationError(
                    "UpdateArticle",
                    "title",
                    "",
                    "Title existed"));
        }

        return Optional.empty();
    }
}
