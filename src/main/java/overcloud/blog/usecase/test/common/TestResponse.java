package overcloud.blog.usecase.test.common;

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

    @JsonProperty("description")
    private String description;

    @JsonProperty("questions")
    private List<Question> questions;

    public static TestResponse testResponseFactory(String title, String description, List<Question> questions) {
        return TestResponse.builder()
                .title(title)
                .description(description)
                .questions(questions)
                .build();
    }
}
