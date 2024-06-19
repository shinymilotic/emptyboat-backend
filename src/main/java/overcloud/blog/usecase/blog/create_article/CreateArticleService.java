package overcloud.blog.usecase.blog.create_article;

import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.entity.*;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.repository.IArticleTagRepository;
import overcloud.blog.repository.jparepository.JpaArticleRepository;
import overcloud.blog.repository.jparepository.JpaArticleTagRepository;
import overcloud.blog.repository.jparepository.JpaTagRepository;
import overcloud.blog.usecase.blog.common.ArticleResMsg;
import overcloud.blog.usecase.blog.common.ArticleUtils;
import overcloud.blog.usecase.blog.common.AuthorResponse;
import overcloud.blog.usecase.blog.common.TagResMsg;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.exceptionhandling.ApiError;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ApiValidationError;
import overcloud.blog.usecase.common.response.ExceptionFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.common.validation.ObjectsValidator;
import overcloud.blog.usecase.user.common.UserError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CreateArticleService {

    private final SpringAuthenticationService authenticationService;

    private final JpaTagRepository tagRepository;

    private final IArticleRepository articleRepository;

    private final ObjectsValidator<ArticleRequest> validator;

    private final IArticleTagRepository articleTagRepository;

    private final ExceptionFactory exceptionFactory;


    public CreateArticleService(SpringAuthenticationService authenticationService,
                                JpaTagRepository tagRepository,
                                IArticleRepository articleRepository,
                                ObjectsValidator<ArticleRequest> validator,
                                IArticleTagRepository articleTagRepository,
                                ExceptionFactory exceptionFactory) {
        this.authenticationService = authenticationService;
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
        this.validator = validator;
        this.articleTagRepository = articleTagRepository;
        this.exceptionFactory = exceptionFactory;
    }

    @Transactional
    public RestResponse<ArticleResponse> createArticle(ArticleRequest articleRequest) {
        String title = articleRequest.getTitle();
        List<String> distinctTags = filterDistinctTags(articleRequest.getTagList());
        articleRequest.setTagList(distinctTags);
        List<ApiValidationError> apiError = validator.validate(articleRequest);

        if (apiError.isPresent()) {
            throw exceptionFactory.invalidDataException(null, apiError);
        }

        Optional<Boolean> isExist = articleRepository.isTitleExist(title);

        if (isExist.isPresent()) {
            throw new InvalidDataException(ArticleResMsg.ARTICLE_TITLE_EXISTS);
        }

        List<TagEntity> tagEntities = tagRepository.findByTagName(articleRequest.getTagList());
        if (distinctTags.size() > tagEntities.size()) {
            throw new InvalidDataException(TagResMsg.TAG_NO_EXISTS);
        }

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(UserError.USER_NOT_FOUND))
                .getUser();
        ArticleEntity articleEntity = toArticleEntity(articleRequest, currentUser, tagEntities);
        List<ArticleTag> articleTags = toArticleTagEntity(tagEntities, articleEntity);
        articleRepository.save(articleEntity);
        articleTagRepository.saveAll(articleTags);
        articleRepository.updateSearchVector();

        return toCreateArticleResponse(articleEntity, currentUser, distinctTags);
    }

    private List<String> filterDistinctTags(List<String> tags) {
        if (tags != null) {
            tags = tags.stream().distinct().toList();
        } else {
            tags = new ArrayList<>();
        }

        return tags;
    }

    public ArticleEntity toArticleEntity(ArticleRequest articleRequest, UserEntity author, List<TagEntity> tagEntities) {
        String title = articleRequest.getTitle();
        String body = articleRequest.getBody();
        String description = articleRequest.getDescription();
        String slug = ArticleUtils.toSlug(title);
        LocalDateTime now = LocalDateTime.now();

        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setId(UuidCreator.getTimeOrderedEpoch());
        articleEntity.setAuthorId(author.getId());
        articleEntity.setBody(body);
        articleEntity.setDescription(description);
        articleEntity.setSlug(slug);
        articleEntity.setTitle(title);
        articleEntity.setCreatedAt(now);
        articleEntity.setUpdatedAt(now);

        return articleEntity;
    }

    public List<ArticleTag> toArticleTagEntity(List<TagEntity> tagEntities, ArticleEntity articleEntity) {
        return tagEntities.stream()
                .map(tagEntity -> {
                    ArticleTagId articleTagId = new ArticleTagId();
                    articleTagId.setArticleId(articleEntity.getId());
                    articleTagId.setTagId(tagEntity.getId());
                    return ArticleTag.builder()
                            .id(articleTagId)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public ArticleResponse toCreateArticleResponse(ArticleEntity articleEntity, UserEntity author, List<String> tagNames) {
        return ArticleResponse.builder()
                .id(articleEntity.getId().toString())
                .title(articleEntity.getTitle())
                .body(articleEntity.getBody())
                .description(articleEntity.getDescription())
                .tagList(tagNames)
                .author(toAuthorResponse(author))
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
