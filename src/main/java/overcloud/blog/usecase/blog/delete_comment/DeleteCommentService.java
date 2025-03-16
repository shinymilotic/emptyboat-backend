package overcloud.blog.usecase.blog.delete_comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.repository.CommentRepository;

import java.util.UUID;

@Service
public class DeleteCommentService {
    private final CommentRepository commentRepository;

    public DeleteCommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Void deleteComment(String commendId) {
        commentRepository.deleteById(UUID.fromString(commendId));
        return null;
    }
}
