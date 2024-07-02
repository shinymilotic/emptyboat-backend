package overcloud.blog.usecase.blog.get_comments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentsResponse {
    private List<CommentResponse> comments;

    public static GetCommentsResponse from(List<CommentResponse> comments) {
        return new GetCommentsResponse(comments);
    }
}
