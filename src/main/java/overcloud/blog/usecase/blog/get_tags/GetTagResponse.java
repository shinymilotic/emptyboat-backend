package overcloud.blog.usecase.blog.get_tags;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GetTagResponse {
    @JsonProperty("tags")
    private Iterable<String> tagList;
}
