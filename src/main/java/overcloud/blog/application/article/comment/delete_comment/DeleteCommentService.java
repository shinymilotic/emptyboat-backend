package overcloud.blog.application.article.comment.delete_comment;

import org.springframework.stereotype.Service;
import overcloud.blog.application.article.comment.core.repository.CommentRepository;

import java.util.UUID;

@Service
public class DeleteCommentService {

    private final CommentRepository commentRepository;

    public DeleteCommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public boolean deleteComment(String slug) {
        commentRepository.deleteByArticleSlug(slug);
        return true;
    }

}
