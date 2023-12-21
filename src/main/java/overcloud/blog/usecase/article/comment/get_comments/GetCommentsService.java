package overcloud.blog.usecase.article.comment.get_comments;

import org.springframework.stereotype.Service;

import overcloud.blog.entity.CommentEntity;
import overcloud.blog.repository.CommentRepository;
import overcloud.blog.usecase.article.comment.core.AuthorResposne;
import overcloud.blog.entity.UserEntity;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetCommentsService {
    private final CommentRepository commentRepository;

    public GetCommentsService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public GetCommentsResponse getComments(String articleSlug) {
        List<CommentEntity> commentEntities = commentRepository.findByArticleSlug(articleSlug);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntities) {
            UserEntity author = commentEntity.getAuthor();
            commentResponses.add(toCommentResponse(commentEntity, author));
        }

        return GetCommentsResponse.from(commentResponses);
    }

    public CommentResponse toCommentResponse(CommentEntity commentEntity, UserEntity authorEntity) {
        return CommentResponse.builder()
                .id(commentEntity.getId())
                .body(commentEntity.getBody())
                .createdAt(commentEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm")))
                .authorResponse(toAuthorResponse(authorEntity))
                .build();
    }

    private AuthorResposne toAuthorResponse(UserEntity authorEntity) {
        return AuthorResposne.builder()
                .username(authorEntity.getUsername())
                .bio(authorEntity.getBio())
                .image(authorEntity.getImage())
                .build();
    }
}
