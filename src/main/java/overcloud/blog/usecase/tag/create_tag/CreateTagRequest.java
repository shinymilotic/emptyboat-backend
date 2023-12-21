package overcloud.blog.usecase.tag.create_tag;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateTagRequest {
    @JsonProperty("tags")
    @NotEmpty(message = "tag.tagList.not-empty")
    private List<String> tags;
}
