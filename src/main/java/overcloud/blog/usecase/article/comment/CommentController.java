package overcloud.blog.usecase.article.comment;

import org.springframework.web.bind.annotation.*;

import overcloud.blog.infrastructure.ApiConst;
import overcloud.blog.usecase.article.comment.create_comment.CreateCommentRequest;
import overcloud.blog.usecase.article.comment.create_comment.CreateCommentResponse;
import overcloud.blog.usecase.article.comment.create_comment.CreateCommentService;
import overcloud.blog.usecase.article.comment.delete_comment.DeleteCommentService;
import overcloud.blog.usecase.article.comment.get_comments.GetCommentsResponse;
import overcloud.blog.usecase.article.comment.get_comments.GetCommentsService;


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
    public CreateCommentResponse createComment(@PathVariable String slug,
                                               @RequestBody CreateCommentRequest createCommentRequest) {
        return createCommentService.createComment(createCommentRequest, slug);
    }

    @GetMapping(ApiConst.ARTICLES_SLUG_COMMENTS)
    public GetCommentsResponse getComments(@PathVariable String slug) {
        return getCommentsService.getComments(slug);
    }

    @DeleteMapping(ApiConst.ARTICLES_SLUG_COMMENTS_ID)
    public boolean deleteComment(@PathVariable String slug, @PathVariable("id") String commendId) {
        return deleteCommentService.deleteComment(slug, commendId);
    }
}
