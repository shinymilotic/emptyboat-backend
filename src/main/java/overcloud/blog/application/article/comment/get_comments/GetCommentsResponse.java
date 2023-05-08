package overcloud.blog.application.article.comment.get_comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetCommentsResponse {
    @JsonProperty("comments")
    private List<GetCommentResponse> comments;
}
