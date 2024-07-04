package overcloud.blog.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.usecase.blog.create_comment.CreateCommentRequest;
import overcloud.blog.usecase.blog.create_comment.CreateCommentResponse;
import overcloud.blog.usecase.blog.create_comment.CreateCommentService;
import overcloud.blog.usecase.blog.delete_comment.DeleteCommentService;
import overcloud.blog.usecase.blog.get_comments.CommentResponse;
import overcloud.blog.usecase.blog.get_comments.GetCommentsService;
import overcloud.blog.usecase.common.response.RestResponse;

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

    @PostMapping(ApiConst.ARTICLES_SLUG_COMMENTS)
    public RestResponse<CreateCommentResponse> createComment(@PathVariable String slug,
                                               @RequestBody CreateCommentRequest createCommentRequest) {
        return createCommentService.createComment(createCommentRequest, slug);
    }

    @GetMapping(ApiConst.ARTICLES_SLUG_COMMENTS)
    public RestResponse<List<CommentResponse>> getComments(@PathVariable String slug) {
        return getCommentsService.getComments(slug);
    }

    @DeleteMapping(ApiConst.ARTICLES_SLUG_COMMENTS_ID)
    public RestResponse<Void> deleteComment(@PathVariable String slug, @PathVariable("id") String commendId) {
        return deleteCommentService.deleteComment(slug, commendId);
    }
}
