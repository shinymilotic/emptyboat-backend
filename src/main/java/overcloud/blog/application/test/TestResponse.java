package overcloud.blog.application.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TestResponse {
    @JsonProperty("title")
    private String title;
}
