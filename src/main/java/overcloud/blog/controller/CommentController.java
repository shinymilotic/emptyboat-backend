package overcloud.blog.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.usecase.blog.create_comment.CreateCommentRequest;
import overcloud.blog.usecase.blog.create_comment.CreateCommentResponse;
import overcloud.blog.usecase.blog.create_comment.CreateCommentService;
import overcloud.blog.usecase.blog.delete_comment.DeleteCommentService;
import overcloud.blog.usecase.blog.get_comments.CommentResponse;
import overcloud.blog.usecase.blog.get_comments.GetCommentsService;

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

    @PostMapping("/articles/comments/{id}")
    public CreateCommentResponse createComment(@PathVariable String id,
                                               @RequestBody CreateCommentRequest createCommentRequest) {
        return createCommentService.createComment(createCommentRequest, id);
    }

    @GetMapping("/articles/comments/{id}")
    public List<CommentResponse> getComments(@PathVariable String id) {
        return getCommentsService.getComments(id);
    }

    @DeleteMapping("/comments/{id}")
    public Void deleteComment(@PathVariable("id") String commendId) {
        return deleteCommentService.deleteComment(commendId);
    }
}
