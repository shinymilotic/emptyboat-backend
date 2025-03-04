package overcloud.blog.usecase.test.create_practice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(as = PracticeChoiceQuestion.class)
public class PracticeChoiceQuestion implements IQuestionPractice {
    @JsonProperty("questionId")
    private String questionId;

    @JsonProperty("questionType")
    private Integer questionType;

    @JsonProperty("answer")
    private List<String> answer;
}
