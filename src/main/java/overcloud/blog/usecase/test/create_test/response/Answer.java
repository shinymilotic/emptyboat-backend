package overcloud.blog.usecase.test.create_test.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Answer {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("truth")
    private boolean truth;

    public static Answer answerFactory(UUID id, String answer, boolean truth) {
        return Answer.builder()
                .id(id)
                .answer(answer)
                .truth(truth)
                .build();
    }
}
