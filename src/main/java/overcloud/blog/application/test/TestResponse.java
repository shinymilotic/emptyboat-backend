package overcloud.blog.application.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TestResponse {
    @JsonProperty("title")
    private String title;

    @JsonProperty("questions")
    private List<Question> questions;
}
