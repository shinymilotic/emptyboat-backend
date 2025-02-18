package overcloud.blog.usecase.blog.create_comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.f4b6a3.uuid.UuidCreator;

import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ApiError;
import overcloud.blog.utils.validation.ObjectsValidator;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.entity.CommentEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.repository.ICommentRepository;
import overcloud.blog.usecase.blog.common.AuthorResponse;
import overcloud.blog.usecase.blog.common.CommentResMsg;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
public class CreateCommentService {
    private final IArticleRepository articleRepository;
    private final ICommentRepository commentRepository;
    private final SpringAuthenticationService authenticationService;
    private final ObjectsValidator<CreateCommentRequest> validator;

    public CreateCommentService(IArticleRepository articleRepository,
                                ICommentRepository commentRepository,
                                SpringAuthenticationService authenticationService,
                                ObjectsValidator<CreateCommentRequest> validator) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.authenticationService = authenticationService;
        this.validator = validator;
    }

    @Transactional
    public CreateCommentResponse createComment(CreateCommentRequest createCommentRequest, String id) {
        Optional<ApiError> apiError = validator.validate(createCommentRequest);

        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        Optional<ArticleEntity> articleEntities = articleRepository.findById(UUID.fromString(id));
        if (articleEntities.isEmpty()) {
            throw validator.fail(CommentResMsg.COMMENT_ARTICLE_NOT_EXIST);
        }

        ArticleEntity articleEntity = articleEntities.get();

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> validator.fail(UserResMsg.USER_NOT_FOUND))
                .getUser();
        CommentEntity savedCommentEntity = saveComment(createCommentRequest, articleEntity, currentUser);

        return toCreateCommentResponse(savedCommentEntity, currentUser);
    }

    public CommentEntity saveComment(CreateCommentRequest createCommentRequest, ArticleEntity articleEntity, UserEntity author) {
        String body = createCommentRequest.getBody();
        LocalDateTime now = LocalDateTime.now();
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentId(UuidCreator.getTimeOrderedEpoch());
        commentEntity.setArticle(articleEntity);
        commentEntity.setAuthor(author);
        commentEntity.setBody(body);
        commentEntity.setCreatedAt(now);
        commentEntity.setUpdatedAt(now);
        return commentRepository.save(commentEntity);
    }

    public CreateCommentResponse toCreateCommentResponse(CommentEntity commentEntity, UserEntity userEntity) {
        return CreateCommentResponse.builder()
                .id(commentEntity.getCommentId())
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
