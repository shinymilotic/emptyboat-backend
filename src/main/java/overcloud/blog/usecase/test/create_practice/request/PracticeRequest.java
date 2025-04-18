package overcloud.blog.usecase.test.create_practice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Builder
@Getter
@Setter
public class PracticeRequest {
    @JsonProperty("testId")
    private String testId;

    @JsonProperty("practices")
    private List<QuestionPractice> practices;
}
