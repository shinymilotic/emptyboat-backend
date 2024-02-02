package overcloud.blog.usecase.test.get_practice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PracticeResult {
    @JsonProperty("practiceId")
    private String practiceId;

    @JsonProperty("testTitle")
    private String testTitle;

    @JsonProperty("questions")
    private List<PracticeQuestion> questions;
}
