package overcloud.blog.usecase.blog.create_comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.blog.common.CommentResMsg;

@Getter
@Setter
public class CreateCommentRequest {
    @JsonProperty("body")
    @NotBlank(message = CommentResMsg.COMMENT_NOTBLANK)
    private String body;
}
