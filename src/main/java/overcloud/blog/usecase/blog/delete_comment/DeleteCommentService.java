package overcloud.blog.usecase.blog.delete_comment;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import overcloud.blog.repository.jparepository.JpaCommentRepository;

import java.util.UUID;

@Service
public class DeleteCommentService {

    private final JpaCommentRepository commentRepository;

    public DeleteCommentService(JpaCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public boolean deleteComment(String slug, String commendId) {
        commentRepository.deleteById(UUID.fromString(commendId));
        return true;
    }
}
