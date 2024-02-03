package overcloud.blog.usecase.test.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChoiceAnswer {
    @JsonProperty("questionId")
    private String questionId;

    @JsonProperty("answerId")
    private List<String> answer;
}
