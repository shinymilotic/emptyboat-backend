package overcloud.blog.application.article.comment.create_comment;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import overcloud.blog.application.article.comment.core.CommentEntity;
import overcloud.blog.application.article.comment.core.repository.CommentRepository;
import overcloud.blog.application.article.core.ArticleEntity;
import overcloud.blog.application.article.core.repository.ArticleRepository;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.time.LocalDateTime;

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
        CreateCommentResponse createCommentResponse = new CreateCommentResponse();
        ArticleEntity articleEntity = articleRepository.findBySlug(slug).get(0);

        UserEntity userEntity = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .orElseThrow(EntityNotFoundException::new)
                .orElseThrow(EntityNotFoundException::new);

        String body = createCommentRequest.getBody();
        LocalDateTime now = LocalDateTime.now();

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setArticle(articleEntity);
        commentEntity.setAuthor(userEntity);
        commentEntity.setBody(body);
        commentEntity.setCreatedAt(now);
        commentEntity.setUpdatedAt(now);

        CommentEntity savedCommentEntity = commentRepository.save(commentEntity);

        CreateCommentAuthorResponse authorResponse = new CreateCommentAuthorResponse();
        authorResponse.setUsername(userEntity.getUsername());
        authorResponse.setBio(userEntity.getBio());
        authorResponse.setImage(userEntity.getImage());

        createCommentResponse.setId(savedCommentEntity.getId());
        createCommentResponse.setAuthor(authorResponse);
        createCommentResponse.setBody(body);
        createCommentResponse.setCreatedAt(savedCommentEntity.getCreatedAt());
        createCommentResponse.setUpdatedAt(savedCommentEntity.getUpdatedAt());

        return createCommentResponse;
    }
}
