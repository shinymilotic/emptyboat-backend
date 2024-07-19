package overcloud.blog.usecase.test.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(as = ChoiceQuestion.class)
public class ChoiceQuestion implements Question {
    @JsonProperty("id")
    private String id;

    @JsonProperty("question")
    @NotBlank(message = TestResMsg.TEST_QUESTION_SIZE)
    private String question;

    @JsonProperty("questionType")
    private int questionType;

    @JsonProperty("answers")
    @NotEmpty(message = TestResMsg.TEST_LIST_ANSWER_SIZE)
    private List<Answer> answers;

    public static ChoiceQuestion questionFactory(String id, String question, List<Answer> answers) {
        return new ChoiceQuestion(id, question, 1, answers);
    }
}
