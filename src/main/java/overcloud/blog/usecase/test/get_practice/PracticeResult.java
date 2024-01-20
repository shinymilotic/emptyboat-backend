package overcloud.blog.usecase.test.get_practice;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
