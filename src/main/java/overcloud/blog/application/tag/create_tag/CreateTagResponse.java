package overcloud.blog.application.tag.create_tag;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CreateTagResponse {
    @JsonProperty("tags")
    private Iterable<String> tags;
}
