package overcloud.blog.usecase.blog.get_comments;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.CommentEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.ICommentRepository;
import overcloud.blog.usecase.blog.common.AuthorResposne;
import overcloud.blog.usecase.blog.common.CommentResMsg;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetCommentsService {
    private final ICommentRepository commentRepository;
    private final ResFactory resFactory;

    public GetCommentsService(ICommentRepository commentRepository, ResFactory resFactory) {
        this.commentRepository = commentRepository;
        this.resFactory = resFactory;
    }

    @Transactional(readOnly = true)
    public RestResponse<GetCommentsResponse> getComments(String articleSlug) {
        List<CommentEntity> commentEntities = commentRepository.findByArticleSlug(articleSlug);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntities) {
            UserEntity author = commentEntity.getAuthor();
            commentResponses.add(toCommentResponse(commentEntity, author));
        }

        return resFactory.success(CommentResMsg.COMMENT_GET_SUCCESS, GetCommentsResponse.from(commentResponses));
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
