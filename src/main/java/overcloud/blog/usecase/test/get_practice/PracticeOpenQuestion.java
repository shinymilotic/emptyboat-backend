package overcloud.blog.usecase.test.get_practice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@JsonDeserialize(as = PracticeOpenQuestion.class)
public class PracticeOpenQuestion implements PracticeQuestion {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("question")
    private String question;

    @JsonProperty("questionType")
    private int questionType;

    @JsonProperty("answer")
    private String answer;

    public static PracticeOpenQuestion questionFactory(UUID id, String question, String answer) {
        return new PracticeOpenQuestion(id, question, 2, answer);
    }
}
