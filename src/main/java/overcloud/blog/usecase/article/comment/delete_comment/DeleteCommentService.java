package overcloud.blog.usecase.article.comment.delete_comment;

import org.springframework.stereotype.Service;
import overcloud.blog.repository.jparepository.JpaCommentRepository;

import java.util.UUID;

@Service
public class DeleteCommentService {

    private final JpaCommentRepository commentRepository;

    public DeleteCommentService(JpaCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public boolean deleteComment(String slug, String commendId) {
        commentRepository.deleteById(UUID.fromString(commendId));
        return true;
    }
}
