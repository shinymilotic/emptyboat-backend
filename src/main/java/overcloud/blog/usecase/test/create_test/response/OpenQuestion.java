package overcloud.blog.usecase.test.create_test.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import overcloud.blog.usecase.test.common.Question;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(as = OpenQuestion.class)
public class OpenQuestion implements Question {
    @JsonProperty("id")
    private String id;

    @JsonProperty("question")
    private String question;

    @JsonProperty("questionType")
    private int questionType;

    public static OpenQuestion questionFactory(String id, String question) {
        return new OpenQuestion(id, question, 2);
    }
}
