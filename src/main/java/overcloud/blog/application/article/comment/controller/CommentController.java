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

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;


    @PostMapping("articles/comments")
    public CreateCommentResponse createComment(@Valid @RequestBody CreateCommentRequest createCommentRequest) {
        return commentService.createComment(createCommentRequest);
    }

    @GetMapping("articles/comments")
    public GetCommentsResponse getComments(@Valid @RequestBody GetCommentRequest getCommentRequest) {
        return commentService.getComments(getCommentRequest.getArticleSLug());
    }

    /*@PutMapping

    */

    @DeleteMapping("articles/comments/{id}")
    public void deleteComment(@PathVariable("id") String id) {
        UUID uuId = UUID.fromString(id);
        commentService.deleteComment(uuId);
    }
}
