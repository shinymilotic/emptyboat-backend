package overcloud.blog.usecase.test.create_test.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.test.common.Question;
import overcloud.blog.usecase.test.common.TestResMsg;

import java.util.List;

@Getter
@Setter
@Builder
public class TestRequest {
    @JsonProperty("title")
    @NotBlank(message = TestResMsg.TEST_TITLE_SIZE)
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("questions")
    @NotEmpty(message = TestResMsg.TEST_LIST_QUESTION_SIZE)
    private List<Question> questions;
}
