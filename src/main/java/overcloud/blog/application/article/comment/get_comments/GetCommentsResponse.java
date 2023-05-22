package overcloud.blog.application.article.comment.get_comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentsResponse {
    @JsonProperty("comments")
    private List<CommentResponse> comments;

    public static GetCommentsResponse from(List<CommentResponse> comments) {
        return new GetCommentsResponse(comments);
    }
}
