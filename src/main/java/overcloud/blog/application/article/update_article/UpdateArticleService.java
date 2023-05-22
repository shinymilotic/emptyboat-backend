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
import overcloud.blog.application.article_tag.ArticleTagId;
import overcloud.blog.application.tag.core.repository.TagRepository;
import overcloud.blog.application.article_tag.ArticleTag;
import overcloud.blog.application.article.core.ArticleEntity;
import overcloud.blog.application.tag.core.TagEntity;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.validation.ObjectsValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UpdateArticleService {

    private final ArticleRepository articleRepository;

    private final TagRepository tagRepository;

    private final ArticleTagRepository articleTagRepository;

    private final ObjectsValidator<ArticleRequest> validator;

    public UpdateArticleService(ArticleRepository articleRepository,
                                TagRepository tagRepository,
                                ArticleTagRepository articleTagRepository,
                                ObjectsValidator<ArticleRequest> validator) {
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
        this.articleTagRepository = articleTagRepository;
        this.validator = validator;
    }

    @Transactional
    public UpdateArticleResponse updateArticle(ArticleRequest updateArticleRequest, String currentSlug) {
        String title = updateArticleRequest.getTitle();
        String body = updateArticleRequest.getBody();
        String description = updateArticleRequest.getDescription();
        String slug = ArticleUtils.toSlug(title);
        LocalDateTime now = LocalDateTime.now();
        Optional<ApiError> apiError = validator.validate(updateArticleRequest);

        if(apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        List<ArticleEntity> articleEntities = articleRepository.findBySlug(slug);

        if(articleEntities.isEmpty()) {
            throw new InvalidDataException(ApiError.from(ArticleError.ARTICLE_NO_EXISTS));
        }

        ArticleEntity articleEntity = articleEntities.get(0);
        articleEntity.setTitle(title);
        articleEntity.setDescription(description);
        articleEntity.setSlug(slug);
        articleEntity.setBody(body);
        articleEntity.setUpdatedAt(now);
        List<ArticleTag> articleTags = articleEntity.getArticleTags();
        deleteArticleTags(articleTags);
        List<TagEntity> tagEntities = tagRepository.findByTagName(updateArticleRequest.getTagList());
        List<ArticleTag> updateArticleTags = tagEntities.stream().map(tagEntity -> ArticleTag.builder()
                                        .id(new ArticleTagId(articleEntity.getId(), tagEntity.getId()))
                                        .article(articleEntity)
                                        .tag(tagEntity)
                                        .build()).map(articleTagRepository::save).collect(Collectors.toList());
        articleEntity.setArticleTags(updateArticleTags);
        articleEntity.setSlug(slug);
        articleRepository.save(articleEntity);

        return toUpdateArticleResponse(articleEntity);
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

    private void deleteArticleTags(List<ArticleTag> articleTags) {
        for (ArticleTag articleTag : articleTags) {
            articleTagRepository.deleteArticleTags(articleTag.getArticle().getId(), articleTag.getTag().getId());
        }
    }
}
