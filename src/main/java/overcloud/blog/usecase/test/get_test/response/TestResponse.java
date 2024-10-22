package overcloud.blog.usecase.test.get_test.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.test.common.Question;
import overcloud.blog.usecase.user.common.UserResponse;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TestResponse {
    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("questions")
    private List<Question> questions;

    @JsonProperty("author")
    private UserResponse author;

    public TestResponse() {
        this.questions = new ArrayList<>();
    }

    public static TestResponse testResponseFactory(String title, String description, List<Question> questions) {
        return TestResponse.builder()
                .title(title)
                .description(description)
                .questions(questions)
                .build();
    }
}
