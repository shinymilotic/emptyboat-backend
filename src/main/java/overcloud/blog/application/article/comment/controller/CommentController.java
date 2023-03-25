package overcloud.blog.application.article.comment.controller;

import overcloud.blog.application.article.comment.dto.create.CreateCommentRequest;
import overcloud.blog.application.article.comment.dto.create.CreateCommentResponse;
import overcloud.blog.application.article.comment.dto.delete.DeleteCommentResponse;
import overcloud.blog.application.article.comment.dto.get.GetCommentRequest;
import overcloud.blog.application.article.comment.dto.get.GetCommentResponse;
import overcloud.blog.application.article.comment.dto.get.GetCommentsResponse;
import overcloud.blog.application.article.comment.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;


    @PostMapping("articles/{slug}/comments")
    public CreateCommentResponse createComment(@PathVariable("slug") String slug,
                                               @Valid @RequestBody CreateCommentRequest createCommentRequest) {
        return commentService.createComment(createCommentRequest, slug);
    }

    @GetMapping("articles/{slug}/comments")
    public GetCommentsResponse getComments(@PathVariable("slug") String slug) {
        return commentService.getComments(slug);
    }

    /*@PutMapping

    */

    @DeleteMapping("articles/comments/{id}")
    public void deleteComment(@PathVariable("id") String id) {
        UUID uuId = UUID.fromString(id);
        commentService.deleteComment(uuId);
    }
}
