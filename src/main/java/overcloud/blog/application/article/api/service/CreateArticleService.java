package overcloud.blog.application.article.api.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import overcloud.blog.application.article.api.dto.AuthorResponse;
import overcloud.blog.application.article.api.dto.create.CreateArticleRequest;
import overcloud.blog.application.article.api.dto.create.CreateArticleResponse;
import overcloud.blog.application.article.api.exception.WriteArticleException;
import overcloud.blog.application.article.api.repository.ArticleRepository;
import overcloud.blog.application.tag.repository.TagRepository;
import overcloud.blog.domain.ArticleTag;
import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.article.tag.TagEntity;
import overcloud.blog.domain.user.UserEntity;
import overcloud.blog.infrastructure.exceptionhandling.dto.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.dto.ApiValidationError;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.string.URLConverter;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CreateArticleService {

    @Autowired
    private SpringAuthenticationService authenticationService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ArticleRepository articleRepository;

    public Optional<ApiError> validate(CreateArticleRequest request) {
        ApiError apiError = ApiError.from("Validation error");

        List<ApiValidationError> apiValidationError = validateCreateArticleDto(request);
        for (ApiValidationError error : apiValidationError) {
            apiError.addApiValidationErrorDetail(error);
        }

        validateTagList(request.getTagList())
                .ifPresent(apiError::addApiValidationErrorDetail);

        validateTitle(request.getTitle())
                .ifPresent(apiError::addApiValidationErrorDetail);

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

        if(tagEntities.size() != 0 && tagEntities.size() != tagList.size()) {
            return Optional.of(ApiValidationError.addValidationError(
                    "CreateArticle",
                    "tagList",
                    "",
                    "tag list doesn't exist"));
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
        CreateArticleResponse response = new CreateArticleResponse();
        String title = articleRequest.getTitle();
        String body = articleRequest.getBody();
        String description = articleRequest.getDescription();
        String slug = URLConverter.toSlug(title);

        Optional<ApiError> apiError = validate(articleRequest);

        if(apiError.isPresent()) {
            throw new WriteArticleException(apiError.get());
        }

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
}
