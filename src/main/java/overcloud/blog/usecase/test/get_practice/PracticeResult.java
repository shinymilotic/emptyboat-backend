package overcloud.blog.usecase.test.get_practice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PracticeResult {
    @JsonProperty("testTitle")
    private String testTitle;

    @JsonProperty("questions")
    private List<PracticeQuestion> questions;
}
