package overcloud.blog.usecase.blog.update_article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.usecase.blog.common.ArticleResMsg;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ApiError;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.common.validation.ObjectsValidator;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateArticleService {
    private final SpringAuthenticationService authenticationService;
    private final IArticleRepository articleRepository;
    private final ObjectsValidator<UpdateArticleRequest> validator;
    private final ResFactory resFactory;

    public UpdateArticleService(SpringAuthenticationService authenticationService,
                                IArticleRepository articleRepository,
                                ObjectsValidator<UpdateArticleRequest> validator,
                                ResFactory resFactory) {
        this.authenticationService = authenticationService;
        this.articleRepository = articleRepository;
        this.validator = validator;
        this.resFactory = resFactory;
    }

    @Transactional
    public RestResponse<UUID> updateArticle(UpdateArticleRequest updateArticleRequest, String id) {
        Optional<ApiError> apiError = validator.validate(updateArticleRequest);
        if (!apiError.isEmpty()) {
            throw new InvalidDataException(apiError.get());
        }

        Optional<ArticleEntity> articleEntities = articleRepository.findById(UUID.fromString(id));
        if (!articleEntities.isPresent()) {
            throw new InvalidDataException(resFactory.fail(ArticleResMsg.ARTICLE_NO_EXISTS));
        }
        ArticleEntity articleEntity = articleEntities.get();

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();

//        // Update authorization
       if (!currentUser.getId().equals(articleEntity.getAuthorId())) {
           throw new InvalidDataException(resFactory.fail(ArticleResMsg.ARTICLE_UPDATE_NO_AUTHORIZATION));
       }

        LocalDateTime now = LocalDateTime.now();
        articleEntity.setBody(updateArticleRequest.getBody());
        articleEntity.setUpdatedAt(now);
        articleRepository.save(articleEntity);
        articleRepository.updateSearchVector();

        return resFactory.success(ArticleResMsg.ARTICLE_UPDATE_SUCCESS, articleEntity.getArticleId());
    }
}
