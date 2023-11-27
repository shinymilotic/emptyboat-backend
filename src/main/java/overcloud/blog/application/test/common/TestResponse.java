package overcloud.blog.application.test.common;

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

    public static TestResponse testResponseFactory(String title, List<Question> questions) {
        return TestResponse.builder()
                .title(title)
                .questions(questions)
                .build();
    }
}
