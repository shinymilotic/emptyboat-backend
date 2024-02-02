package overcloud.blog.usecase.test.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
    private String question;

    @JsonProperty("questionType")
    private int questionType;

    @JsonProperty("answers")
    private List<Answer> answers;

    public static ChoiceQuestion questionFactory(String id, String question, List<Answer> answers) {
        return new ChoiceQuestion(id, question, 1, answers);
    }
}
