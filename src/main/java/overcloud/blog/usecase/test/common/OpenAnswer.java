package overcloud.blog.usecase.test.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenAnswer {
    @JsonProperty("questionId")
    private String questionId;

    @JsonProperty("answer")
    private String answer;
}
