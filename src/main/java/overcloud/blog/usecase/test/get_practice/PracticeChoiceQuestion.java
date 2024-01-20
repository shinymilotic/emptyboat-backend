package overcloud.blog.usecase.test.get_practice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.test.common.Answer;

@Getter
@Setter
@AllArgsConstructor
@JsonDeserialize(as = PracticeChoiceQuestion.class)
public class PracticeChoiceQuestion implements PracticeQuestion {
    @JsonProperty("id")
    private String id;

    @JsonProperty("question")
    private String question;

    @JsonProperty("questionType")
    private int questionType;

    @JsonProperty("answer")
    private Answer answer;

    public static PracticeChoiceQuestion questionFactory(String id, String question, Answer answer) {
        return new PracticeChoiceQuestion(id, question, 1, answer);
    }
}
