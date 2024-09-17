package overcloud.blog.usecase.blog.delete_comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.common.response.ResFactory;
import overcloud.blog.common.response.RestResponse;
import overcloud.blog.repository.ICommentRepository;
import overcloud.blog.usecase.blog.common.CommentResMsg;

import java.util.UUID;

@Service
public class DeleteCommentService {
    private final ICommentRepository commentRepository;
    private final ResFactory resFactory;

    public DeleteCommentService(ICommentRepository commentRepository, ResFactory resFactory) {
        this.commentRepository = commentRepository;
        this.resFactory = resFactory;
    }

    @Transactional
    public RestResponse<Void> deleteComment(String commendId) {
        commentRepository.deleteById(UUID.fromString(commendId));
        return resFactory.success(CommentResMsg.COMMENT_DELETE_SUCCESS, null);
    }
}
