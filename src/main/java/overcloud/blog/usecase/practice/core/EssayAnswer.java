package overcloud.blog.usecase.practice.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EssayAnswer {
    @JsonProperty("questionId")
    private String questionId;

    @JsonProperty("answer")
    private String answer;
}
