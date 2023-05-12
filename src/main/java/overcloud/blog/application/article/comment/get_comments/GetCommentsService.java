package overcloud.blog.application.article.comment.get_comments;

import org.springframework.stereotype.Service;
import overcloud.blog.application.article.comment.core.CommentEntity;
import overcloud.blog.application.article.comment.core.repository.CommentRepository;
import overcloud.blog.application.user.core.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetCommentsService {


    private final CommentRepository commentRepository;


    public GetCommentsService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public GetCommentsResponse getComments(String articleSlug) {
        GetCommentsResponse getCommentsResponse = new GetCommentsResponse();
        getCommentsResponse.setComments(new ArrayList<>());

        getCommentsResponse.setComments(new ArrayList<>());

        List<CommentEntity> commentEntities = commentRepository.findByArticleSlug(articleSlug);

        for (CommentEntity commentEntity : commentEntities) {
            UserEntity author = commentEntity.getAuthor();
            CommentResponse getCommentResponse = new CommentResponse();

            AuthorResposne authorResponse = new AuthorResposne();
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
}
