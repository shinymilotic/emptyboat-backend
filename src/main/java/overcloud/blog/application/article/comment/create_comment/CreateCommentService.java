package overcloud.blog.application.article.comment.create_comment;

import org.springframework.stereotype.Service;
import overcloud.blog.application.article.comment.core.CommentEntity;
import overcloud.blog.application.article.comment.core.repository.CommentRepository;
import overcloud.blog.application.article.core.ArticleEntity;
import overcloud.blog.application.article.core.AuthorResponse;
import overcloud.blog.application.article.core.exception.InvalidDataException;
import overcloud.blog.application.article.core.repository.ArticleRepository;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserError;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CreateCommentService {

    private final ArticleRepository articleRepository;

    private final CommentRepository commentRepository;

    private final SpringAuthenticationService authenticationService;

    public CreateCommentService(ArticleRepository articleRepository,
                                CommentRepository commentRepository,
                                SpringAuthenticationService authenticationService) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.authenticationService = authenticationService;
    }

    public CreateCommentResponse createComment(CreateCommentRequest createCommentRequest, String slug) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug).get(0);
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        String body = createCommentRequest.getBody();
        LocalDateTime now = LocalDateTime.now();
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setArticle(articleEntity);
        commentEntity.setAuthor(currentUser);
        commentEntity.setBody(body);
        commentEntity.setCreatedAt(now);
        commentEntity.setUpdatedAt(now);
        CommentEntity savedCommentEntity = commentRepository.save(commentEntity);

        return toCreateCommentResponse(savedCommentEntity, currentUser);
    }

    public CreateCommentResponse toCreateCommentResponse(CommentEntity commentEntity, UserEntity userEntity){
        return CreateCommentResponse.builder()
                .id(commentEntity.getId())
                .body(commentEntity.getBody())
                .author(toAuthorResponse(userEntity))
                .createdAt(commentEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm")))
                .updatedAt(commentEntity.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm")))
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
