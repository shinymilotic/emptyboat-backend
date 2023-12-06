package overcloud.blog.application.test.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Answer {
    @JsonProperty("id")
    private String id;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("truth")
    private boolean truth;

    public static Answer answerFactory(String id, String answer, boolean truth) {
        return Answer.builder()
                .id(id)
                .answer(answer)
                .truth(truth)
                .build();
    }
}
