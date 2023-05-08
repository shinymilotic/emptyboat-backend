package overcloud.blog.application.article.comment;

import overcloud.blog.application.article.comment.create_comment.CreateCommentRequest;
import overcloud.blog.application.article.comment.create_comment.CreateCommentResponse;
import overcloud.blog.application.article.comment.create_comment.CreateCommentService;
import overcloud.blog.application.article.comment.delete_comment.DeleteCommentService;
import overcloud.blog.application.article.comment.get_comments.GetCommentsResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.application.article.comment.get_comments.GetCommentsService;

import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class CommentController {

    private final GetCommentsService getCommentsService;

    private final DeleteCommentService deleteCommentService;

    private final CreateCommentService createCommentService;

    public CommentController(GetCommentsService getCommentsService,
                             DeleteCommentService deleteCommentService,
                             CreateCommentService createCommentService) {
        this.getCommentsService = getCommentsService;
        this.deleteCommentService = deleteCommentService;
        this.createCommentService = createCommentService;
    }

    @PostMapping("articles/{slug}/comments")
    public CreateCommentResponse createComment(@PathVariable("slug") String slug,
                                               @Valid @RequestBody CreateCommentRequest createCommentRequest) {
        return createCommentService.createComment(createCommentRequest, slug);
    }

    @GetMapping("articles/{slug}/comments")
    public GetCommentsResponse getComments(@PathVariable("slug") String slug) {
        return getCommentsService.getComments(slug);
    }

    @DeleteMapping("articles/comments/{id}")
    public void deleteComment(@PathVariable("id") String id) {
        UUID uuId = UUID.fromString(id);
        deleteCommentService.deleteComment(uuId);
    }
}
