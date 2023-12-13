package overcloud.blog.application.test.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonDeserialize(as = EssayQuestion.class)
public class EssayQuestion implements Question {
    @JsonProperty("question")
    private String question;

    @JsonProperty("questionType")
    private int questionType;

    public static EssayQuestion questionFactory(String question) {
        return new EssayQuestion(question, 2);
    }

}
