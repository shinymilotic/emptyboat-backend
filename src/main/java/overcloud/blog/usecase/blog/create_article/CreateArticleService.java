package overcloud.blog.usecase.blog.create_article;

import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ApiError;
import overcloud.blog.utils.validation.ObjectsValidator;
import overcloud.blog.entity.*;
import overcloud.blog.repository.ArticleRepository;
import overcloud.blog.repository.ArticleTagRepository;
import overcloud.blog.repository.TagRepository;
import overcloud.blog.usecase.blog.common.ArticleResMsg;
import overcloud.blog.usecase.blog.common.TagResMsg;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    @Transactional
    public UUID createArticle(ArticleRequest articleRequest) {
        String title = articleRequest.getTitle();
        List<String> distinctTags = filterDistinctTags(articleRequest.getTagList());
        articleRequest.setTagList(distinctTags);
        Optional<ApiError> apiError = validator.validate(articleRequest);

        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        Optional<Boolean> isExist = articleRepository.isTitleExist(title);

        if (isExist.isPresent()) {
            throw validator.fail(ArticleResMsg.ARTICLE_TITLE_EXISTS);
        }

        List<TagEntity> tagEntities = tagRepository.findByTagIds(articleRequest.getTagList());
        if (distinctTags.size() > tagEntities.size()) {
            throw validator.fail(TagResMsg.TAG_NO_EXISTS);
        }

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> validator.fail(UserResMsg.USER_NOT_FOUND))
                .getUser();
        ArticleEntity articleEntity = toArticleEntity(articleRequest, currentUser);
        List<ArticleTag> articleTags = toArticleTagEntity(tagEntities, articleEntity);
        articleRepository.save(articleEntity);
        articleTagRepository.saveAll(articleTags);
        articleRepository.updateSearchVector();

        return articleEntity.getArticleId();
    }

    private List<String> filterDistinctTags(List<String> tags) {
        if (tags != null) {
            tags = tags.stream().distinct().toList();
        } else {
            tags = new ArrayList<>();
        }

        return tags;
    }

    public ArticleEntity toArticleEntity(ArticleRequest articleRequest, UserEntity author) {
        String title = articleRequest.getTitle();
        String body = articleRequest.getBody();
        String description = articleRequest.getDescription();
        LocalDateTime now = LocalDateTime.now();

        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setArticleId(UuidCreator.getTimeOrderedEpoch());
        articleEntity.setAuthorId(author.getUserId());
        articleEntity.setBody(body);
        articleEntity.setDescription(description);
        articleEntity.setTitle(title);
        articleEntity.setCreatedAt(now);
        articleEntity.setUpdatedAt(now);

        return articleEntity;
    }

    public List<ArticleTag> toArticleTagEntity(List<TagEntity> tagEntities, ArticleEntity articleEntity) {
        return tagEntities.stream()
                .map(tagEntity -> {
                    ArticleTagId articleTagId = new ArticleTagId();
                    articleTagId.setArticleId(articleEntity.getArticleId());
                    articleTagId.setTagId(tagEntity.getTagId());
                    return new ArticleTag(articleTagId);
                })
                .collect(Collectors.toList());
    }
}
