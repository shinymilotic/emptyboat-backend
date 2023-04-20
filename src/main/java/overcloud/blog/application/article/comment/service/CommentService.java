package overcloud.blog.application.article.comment.service;

import jakarta.persistence.EntityNotFoundException;
import overcloud.blog.application.article.comment.dto.create.CreateCommentAuthorResponse;
import overcloud.blog.application.article.comment.dto.create.CreateCommentRequest;
import overcloud.blog.application.article.comment.dto.create.CreateCommentResponse;
import overcloud.blog.application.article.api.repository.ArticleRepository;
import overcloud.blog.application.article.comment.dto.get.GetCommentAuthorResponse;
import overcloud.blog.application.article.comment.dto.get.GetCommentResponse;
import overcloud.blog.application.article.comment.dto.get.GetCommentsResponse;
import overcloud.blog.application.article.comment.repository.CommentRepository;
import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.article.comment.CommentEntity;
import overcloud.blog.domain.user.UserEntity;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    private final ArticleRepository articleRepository;

    private final CommentRepository commentRepository;

    private final SpringAuthenticationService authenticationService;

    public CommentService(ArticleRepository articleRepository,
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

    public GetCommentsResponse getComments(String articleSlug) {
        GetCommentsResponse getCommentsResponse = new GetCommentsResponse();
        getCommentsResponse.setComments(new ArrayList<>());

        getCommentsResponse.setComments(new ArrayList<>());

        List<CommentEntity> commentEntities = commentRepository.findByArticleSlug(articleSlug);

        for (CommentEntity commentEntity : commentEntities) {
            UserEntity author = commentEntity.getAuthor();
            GetCommentResponse getCommentResponse = new GetCommentResponse();

            GetCommentAuthorResponse authorResponse = new GetCommentAuthorResponse();
            authorResponse.setUsername(author.getUsername());
            authorResponse.setBio(author.getBio());
            authorResponse.setImage(author.getImage());

            getCommentResponse.setId(commentEntity.getId());
            getCommentResponse.setBody(commentEntity.getBody());
            getCommentResponse.setCreatedAt(commentEntity.getCreatedAt());
            getCommentResponse.setUpdatedAt(commentEntity.getUpdatedAt());
            getCommentResponse.setAuthorResponse(authorResponse);
            getCommentsResponse.getComments().add(getCommentResponse);
        }

        return getCommentsResponse;
    }

    public void deleteComment(UUID uuId) {
        commentRepository.deleteById(uuId);
    }
}
