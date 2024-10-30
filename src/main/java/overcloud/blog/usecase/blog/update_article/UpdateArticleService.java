package overcloud.blog.usecase.blog.update_article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ApiError;
import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
import overcloud.blog.utils.validation.ObjectsValidator;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.entity.ArticleTag;
import overcloud.blog.entity.ArticleTagId;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.repository.IArticleTagRepository;
import overcloud.blog.repository.ITagRepository;
import overcloud.blog.usecase.blog.common.ArticleResMsg;
import overcloud.blog.usecase.blog.common.TagResMsg;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateArticleService {
    private final SpringAuthenticationService authenticationService;
    private final IArticleRepository articleRepository;
    private final ITagRepository tagRepository;
    private final IArticleTagRepository articleTagRepository;
    private final ObjectsValidator<UpdateArticleRequest> validator;
    private final ResFactory resFactory;

    public UpdateArticleService(SpringAuthenticationService authenticationService,
                                IArticleRepository articleRepository,
                                ITagRepository tagRepository,
                                IArticleTagRepository articleTagRepository,
                                ObjectsValidator<UpdateArticleRequest> validator,
                                ResFactory resFactory) {
        this.authenticationService = authenticationService;
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
        this.articleTagRepository = articleTagRepository;
        this.validator = validator;
        this.resFactory = resFactory;
    }

    @Transactional
    public RestResponse<Void> updateArticle(UpdateArticleRequest updateArticleRequest, String id) {
        Optional<ApiError> apiError = validator.validate(updateArticleRequest);
        if (!apiError.isEmpty()) {
            throw new InvalidDataException(apiError.get());
        }
        List<String> tags = updateArticleRequest.getTagList();
        Optional<ArticleEntity> articleEntities = articleRepository.findById(UUID.fromString(id));
        List<TagEntity> tagEntities = tagRepository.findByTagIds(updateArticleRequest.getTagList());
        
        if (!articleEntities.isPresent()) {
            throw new InvalidDataException(resFactory.fail(ArticleResMsg.ARTICLE_NO_EXISTS));
        }
        ArticleEntity articleEntity = articleEntities.get();

        List<ArticleTag> articleTags = new ArrayList<>();
        for (String tag : tags) {
            UUID tagId = UUID.fromString(tag);
            Optional<TagEntity> tagEntity = isTagExist(tagId, tagEntities);

            if (!tagEntity.isPresent()) {
                throw new InvalidDataException(resFactory.fail(TagResMsg.TAG_NO_EXISTS));
            } else {
                ArticleTagId articleTagId = new ArticleTagId(articleEntity.getArticleId(), tagEntity.get().getTagId());
                articleTags.add(new ArticleTag(articleTagId));
            }
        }

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();

       if (!currentUser.getUserId().equals(articleEntity.getAuthorId())) {
           throw new InvalidDataException(resFactory.fail(ArticleResMsg.ARTICLE_UPDATE_NO_AUTHORIZATION));
       }

        LocalDateTime now = LocalDateTime.now();
        articleEntity.setTitle(updateArticleRequest.getTitle());
        articleEntity.setDescription(updateArticleRequest.getDescription());
        articleEntity.setBody(updateArticleRequest.getBody());
        articleEntity.setUpdatedAt(now);
        
        articleRepository.save(articleEntity);
        articleTagRepository.deleteByArticleId(articleEntity.getArticleId());
        articleTagRepository.saveAll(articleTags);
        articleRepository.updateSearchVector();

        return resFactory.success(ArticleResMsg.ARTICLE_UPDATE_SUCCESS, null);
    }

    private Optional<TagEntity> isTagExist(UUID tagId, List<TagEntity> tagEntities) {
        for (TagEntity tagEntity : tagEntities) {
            if (tagEntity.getTagId().equals(tagId)) {
                return Optional.of(tagEntity);
            }
        }
        return Optional.empty();
    }
}
