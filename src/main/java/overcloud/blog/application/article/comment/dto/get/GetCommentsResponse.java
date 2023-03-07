package overcloud.blog.application.article.comment.dto.get;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetCommentsResponse {

    @JsonProperty("comments")
    private List<GetCommentResponse> comments;

    public List<GetCommentResponse> getComments() {
        return comments;
    }

    public void setComments(List<GetCommentResponse> comments) {
        this.comments = comments;
    }
}
