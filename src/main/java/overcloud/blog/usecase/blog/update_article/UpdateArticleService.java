package overcloud.blog.usecase.blog.update_article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.usecase.blog.common.ArticleResMsg;
import overcloud.blog.usecase.blog.common.AuthorResponse;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ApiError;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.common.validation.ObjectsValidator;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public RestResponse<UpdateArticleResponse> updateArticle(UpdateArticleRequest updateArticleRequest, String currentSlug) {
        Optional<ApiError> apiError = validator.validate(updateArticleRequest);
        if (!apiError.isEmpty()) {
            throw new InvalidDataException(apiError.get());
        }

        List<ArticleEntity> articleEntities = articleRepository.findBySlug(currentSlug);
        if (articleEntities.isEmpty()) {
            throw new InvalidDataException(resFactory.fail(ArticleResMsg.ARTICLE_NO_EXISTS));
        }
        ArticleEntity articleEntity = articleEntities.get(0);

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();

//        // Update authorization
    //    if (!currentUser.getId().equals(articleEntity.getAuthor().getId())) {
    //        throw new InvalidDataException(resFactory.fail(ArticleResMsg.ARTICLE_UPDATE_NO_AUTHORIZATION));
    //    }

        LocalDateTime now = LocalDateTime.now();
        articleEntity.setBody(updateArticleRequest.getBody());
        articleEntity.setUpdatedAt(now);
        articleRepository.save(articleEntity);
        articleRepository.updateSearchVector();

        return resFactory.success(ArticleResMsg.ARTICLE_UPDATE_SUCCESS, toUpdateArticleResponse(currentUser, articleEntity, new ArrayList<String>()));
    }

    private UpdateArticleResponse toUpdateArticleResponse(UserEntity currentUser, ArticleEntity articleEntity, List<String> tagNames) {
        return UpdateArticleResponse.builder()
                .id(articleEntity.getId().toString())
                .title(articleEntity.getTitle())
                .body(articleEntity.getBody())
                .description(articleEntity.getDescription())
                .tagList(tagNames)
                .author(toAuthorResponse(currentUser))
                .slug(articleEntity.getSlug())
                .createdAt(articleEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm")))
                .updatedAt(articleEntity.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm")))
//                .favorited(favoriteUtils.isFavorited(currentUser, articleEntity))
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
