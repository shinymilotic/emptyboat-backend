package overcloud.blog.application.test.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(as = ChoiceQuestion.class)
public class ChoiceQuestion implements Question {
    @JsonProperty("question")
    private String question;

    @JsonProperty("questionType")
    private int questionType;

    @JsonProperty("answers")
    private List<Answer> answers;

    public static ChoiceQuestion questionFactory(String question, List<Answer> answers) {
        return new ChoiceQuestion(question, 1, answers);
    }
}
