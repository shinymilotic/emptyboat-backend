package overcloud.blog.usecase.test.create_practice.request;

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
