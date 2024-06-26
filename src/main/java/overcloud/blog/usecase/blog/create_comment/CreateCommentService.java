package overcloud.blog.usecase.blog.create_comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.entity.CommentEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.repository.ICommentRepository;
import overcloud.blog.usecase.blog.common.AuthorResponse;
import overcloud.blog.usecase.blog.common.CommentResMsg;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ApiError;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.common.validation.ObjectsValidator;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class CreateCommentService {
    private final IArticleRepository articleRepository;
    private final ICommentRepository commentRepository;
    private final SpringAuthenticationService authenticationService;
    private final ObjectsValidator<CreateCommentRequest> validator;
    private final ResFactory resFactory;

    public CreateCommentService(IArticleRepository articleRepository,
                                ICommentRepository commentRepository,
                                SpringAuthenticationService authenticationService,
                                ObjectsValidator<CreateCommentRequest> validator,
                                ResFactory resFactory) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.authenticationService = authenticationService;
        this.validator = validator;
        this.resFactory = resFactory;
    }

    @Transactional
    public RestResponse<CreateCommentResponse> createComment(CreateCommentRequest createCommentRequest, String slug) {
        Optional<ApiError> apiError = validator.validate(createCommentRequest);

        if (apiError.isPresent()) {
            throw new InvalidDataException(resFactory.fail(CommentResMsg.COMMENT_CREATE_FAILED, apiError.get()));
        }

        List<ArticleEntity> articleEntities = articleRepository.findBySlug(slug);
        if (articleEntities.isEmpty()) {
            throw new InvalidDataException(resFactory.fail(CommentResMsg.COMMENT_ARTICLE_NOT_EXIST));
        }

        ArticleEntity articleEntity = articleEntities.get(0);

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();
        CommentEntity savedCommentEntity = saveComment(createCommentRequest, articleEntity, currentUser);

        return resFactory.success(CommentResMsg.COMMENT_SUCCESS, toCreateCommentResponse(savedCommentEntity, currentUser));
    }

    public CommentEntity saveComment(CreateCommentRequest createCommentRequest, ArticleEntity articleEntity, UserEntity author) {
        String body = createCommentRequest.getBody();
        LocalDateTime now = LocalDateTime.now();
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setArticle(articleEntity);
        commentEntity.setAuthor(author);
        commentEntity.setBody(body);
        commentEntity.setCreatedAt(now);
        commentEntity.setUpdatedAt(now);
        return commentRepository.save(commentEntity);
    }

    public CreateCommentResponse toCreateCommentResponse(CommentEntity commentEntity, UserEntity userEntity) {
        return CreateCommentResponse.builder()
                .id(commentEntity.getId())
                .body(commentEntity.getBody())
                .author(toAuthorResponse(userEntity))
                .createdAt(commentEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm")))
                .build();
    }

    private AuthorResponse toAuthorResponse(UserEntity userEntity) {
        return AuthorResponse.builder()
                .username(userEntity.getUsername())
                .bio(userEntity.getBio())
                .image(userEntity.getImage())
                .build();
    }
}
