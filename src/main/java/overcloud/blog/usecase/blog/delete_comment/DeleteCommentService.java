package overcloud.blog.usecase.blog.delete_comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
import overcloud.blog.repository.ICommentRepository;
import overcloud.blog.usecase.blog.common.CommentResMsg;

import java.util.UUID;

@Service
public class DeleteCommentService {
    private final ICommentRepository commentRepository;

    public DeleteCommentService(ICommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Void deleteComment(String commendId) {
        commentRepository.deleteById(UUID.fromString(commendId));
        return null;
    }
}
