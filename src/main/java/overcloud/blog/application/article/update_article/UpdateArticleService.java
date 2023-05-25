package overcloud.blog.application.article.update_article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.application.article.core.ArticleError;
import overcloud.blog.application.article.core.AuthorResponse;
import overcloud.blog.application.article.core.exception.InvalidDataException;
import overcloud.blog.application.article.core.repository.ArticleRepository;
import overcloud.blog.application.article.core.repository.ArticleTagRepository;
import overcloud.blog.application.article.core.utils.ArticleUtils;
import overcloud.blog.application.article.create_article.ArticleRequest;
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
public class UpdateArticleService {
    private final SpringAuthenticationService authenticationService;

    private final ArticleRepository articleRepository;

    private final TagRepository tagRepository;

    private final ArticleTagRepository articleTagRepository;

    private final ObjectsValidator<UpdateArticleRequest> validator;

    public UpdateArticleService(SpringAuthenticationService authenticationService,
                                ArticleRepository articleRepository,
                                TagRepository tagRepository,
                                ArticleTagRepository articleTagRepository,
                                ObjectsValidator<UpdateArticleRequest> validator) {
        this.authenticationService = authenticationService;
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
        this.articleTagRepository = articleTagRepository;
        this.validator = validator;
    }

    @Transactional
    public UpdateArticleResponse updateArticle(UpdateArticleRequest updateArticleRequest, String currentSlug) {
        Optional<ApiError> apiError = validator.validate(updateArticleRequest);
        if(apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        List<ArticleEntity> articleEntities = articleRepository.findBySlug(currentSlug);
        if(articleEntities.isEmpty()) {
            throw new InvalidDataException(ApiError.from(ArticleError.ARTICLE_NO_EXISTS));
        }
        ArticleEntity articleEntity = articleEntities.get(0);

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();
        if (currentUser.getId().equals(articleEntity.getAuthor().getId())) {
            throw new InvalidDataException(ApiError.from(ArticleError.ARTICLE_UPDATE_NO_AUTHORIZATION));
        }

        LocalDateTime now = LocalDateTime.now();
        articleEntity.setBody(updateArticleRequest.getBody());
        articleEntity.setUpdatedAt(now);
        ArticleEntity savedArticleEntity = articleRepository.save(articleEntity);

        return toUpdateArticleResponse(savedArticleEntity);
    }

    private UpdateArticleResponse toUpdateArticleResponse(ArticleEntity articleEntity) {
        return UpdateArticleResponse.builder()
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
