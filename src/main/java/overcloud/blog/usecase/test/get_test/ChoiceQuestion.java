package overcloud.blog.usecase.test.get_test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import overcloud.blog.usecase.test.common.Answer;
import overcloud.blog.usecase.test.common.Question;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@JsonDeserialize(as = ChoiceQuestion.class)
public class ChoiceQuestion implements Question {
    @JsonProperty("id")
    private String id;

    @JsonProperty("question")
    private String question;

    @JsonProperty("questionType")
    private int questionType;

    @JsonProperty("answers")
    private List<Answer> answers;

    @JsonProperty("isMultipleAnswers")
    private boolean isMultipleAnswers;

    public static ChoiceQuestion questionFactory(String id, String question, List<Answer> answers, boolean isMultipleAnswers) {
        return new ChoiceQuestion(id, question, 1, answers, isMultipleAnswers);
    }
}
