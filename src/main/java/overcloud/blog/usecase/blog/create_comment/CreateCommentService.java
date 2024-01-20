package overcloud.blog.usecase.blog.create_comment;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import overcloud.blog.entity.CommentEntity;
import overcloud.blog.repository.jparepository.JpaCommentRepository;
import overcloud.blog.usecase.blog.common.CommentError;
import overcloud.blog.usecase.blog.common.AuthorResponse;
import overcloud.blog.usecase.auth.common.UserError;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.repository.jparepository.JpaArticleRepository;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.validation.ObjectsValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class CreateCommentService {

    private final JpaArticleRepository articleRepository;

    private final JpaCommentRepository commentRepository;

    private final SpringAuthenticationService authenticationService;

    private final ObjectsValidator<CreateCommentRequest> validator;

    public CreateCommentService(JpaArticleRepository articleRepository,
                                JpaCommentRepository commentRepository,
                                SpringAuthenticationService authenticationService,
                                ObjectsValidator<CreateCommentRequest> validator) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.authenticationService = authenticationService;
        this.validator = validator;
    }

    @Transactional
    public CreateCommentResponse createComment(CreateCommentRequest createCommentRequest, String slug) {
        Optional<ApiError> apiError = validator.validate(createCommentRequest);

        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        List<ArticleEntity> articleEntities = articleRepository.findBySlug(slug);
        if(articleEntities.isEmpty()) {
            throw new InvalidDataException(ApiError.from(CommentError.COMMENT_ARTICLE_NOT_EXIST));
        }

        ArticleEntity articleEntity = articleEntities.get(0);

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();
        CommentEntity savedCommentEntity = saveComment(createCommentRequest, articleEntity, currentUser);

        return toCreateCommentResponse(savedCommentEntity, currentUser);
    }

    public CommentEntity saveComment(CreateCommentRequest createCommentRequest,ArticleEntity articleEntity, UserEntity author ) {
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

    public CreateCommentResponse toCreateCommentResponse(CommentEntity commentEntity, UserEntity userEntity){
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
