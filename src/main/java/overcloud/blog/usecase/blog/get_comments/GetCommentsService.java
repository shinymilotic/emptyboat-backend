package overcloud.blog.usecase.blog.get_comments;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.entity.CommentEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.CommentRepository;
import overcloud.blog.usecase.blog.common.AuthorResposne;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GetCommentsService {
    private final CommentRepository commentRepository;

    public GetCommentsService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(String id) {
        List<CommentEntity> commentEntities = commentRepository.findByArticleId(UUID.fromString(id));
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntities) {
            UserEntity author = commentEntity.getAuthor();
            commentResponses.add(toCommentResponse(commentEntity, author));
        }

        return commentResponses;
    }

    public CommentResponse toCommentResponse(CommentEntity commentEntity, UserEntity authorEntity) {
        return CommentResponse.builder()
                .id(commentEntity.getCommentId())
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
