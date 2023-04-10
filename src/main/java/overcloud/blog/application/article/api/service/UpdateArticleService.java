package overcloud.blog.application.article.api.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.application.article.api.dto.AuthorResponse;
import overcloud.blog.application.article.api.dto.update.UpdateArticleRequest;
import overcloud.blog.application.article.api.dto.update.UpdateArticleResponse;
import overcloud.blog.application.article.api.exception.ArticleError;
import overcloud.blog.application.article.api.exception.WriteArticleException;
import overcloud.blog.application.article.api.repository.ArticleRepository;
import overcloud.blog.application.article.api.repository.ArticleTagRepository;
import overcloud.blog.application.tag.repository.TagRepository;
import overcloud.blog.domain.ArticleTag;
import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.article.tag.TagEntity;
import overcloud.blog.domain.user.UserEntity;
import overcloud.blog.infrastructure.exceptionhandling.dto.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.dto.ApiValidationError;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.string.URLConverter;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UpdateArticleService {

    private final SpringAuthenticationService authenticationService;

    private final ArticleRepository articleRepository;

    private final TagRepository tagRepository;

    private final ArticleTagRepository articleTagRepository;

    public UpdateArticleService(SpringAuthenticationService authenticationService,
                                ArticleRepository articleRepository,
                                TagRepository tagRepository,
                                ArticleTagRepository articleTagRepository) {
        this.authenticationService = authenticationService;
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
        this.articleTagRepository = articleTagRepository;
    }

    @Transactional
    public UpdateArticleResponse updateArticle(UpdateArticleRequest updateArticleRequest, String currentSlug) {
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
        Optional<ApiError> apiError = validate(updateArticleRequest, currentSlug, slug);

        if(apiError.isPresent()) {
            throw new WriteArticleException(apiError.get());
        }

        UserEntity currentUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .orElseThrow(EntityNotFoundException::new)
                .orElseThrow(EntityNotFoundException::new);

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
        authorResponse.setBio(currentUser.getBio());
        authorResponse.setUsername(currentUser.getUsername());
        authorResponse.setImage(currentUser.getImage());

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
