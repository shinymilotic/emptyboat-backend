package overcloud.blog.usecase.test.get_practice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.test.common.Answer;

import java.util.UUID;

@Getter
@Setter
@Builder
public class PracticeAnswer {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("truth")
    private Boolean truth;

    @JsonProperty("choice")
    private Boolean isRightChoice;

    public static PracticeAnswer answerFactory(UUID id, String answer, Boolean truth, Boolean isRightChoice) {
        return PracticeAnswer.builder()
                .id(id)
                .answer(answer)
                .truth(truth)
                .isRightChoice(isRightChoice)
                .build();
    }
}
