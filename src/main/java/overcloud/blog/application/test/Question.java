package overcloud.blog.application.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Question {
    @JsonProperty("question")
    private String question;

    @JsonProperty("answers")
    private List<Answer> answers;
}
