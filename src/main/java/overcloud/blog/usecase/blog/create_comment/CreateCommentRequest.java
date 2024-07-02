package overcloud.blog.usecase.blog.create_comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequest {
    @JsonProperty("body")
    @NotNull
    @NotBlank
    private String body;
}
