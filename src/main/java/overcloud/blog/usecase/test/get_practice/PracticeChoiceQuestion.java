package overcloud.blog.usecase.test.get_practice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.test.common.Answer;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@JsonDeserialize(as = PracticeChoiceQuestion.class)
public class PracticeChoiceQuestion implements PracticeQuestion {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("question")
    private String question;

    @JsonProperty("questionType")
    private int questionType;

    @JsonProperty("answers")
    private List<Answer> answers;

    public static PracticeChoiceQuestion questionFactory(UUID id, String question, List<Answer> answers) {
        return new PracticeChoiceQuestion(id, question, 1, answers);
    }
}
