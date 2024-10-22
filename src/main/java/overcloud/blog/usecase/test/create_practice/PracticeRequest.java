package overcloud.blog.usecase.test.create_practice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PracticeRequest {
    @JsonProperty("testId")
    private String testId;

    @JsonProperty("answer")
    private Answer answers;
}
