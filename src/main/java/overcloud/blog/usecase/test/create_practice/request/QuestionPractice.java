package overcloud.blog.usecase.test.create_practice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionPractice<T> {
    @JsonProperty("questionId")
    private String questionId;

    @JsonProperty("questionType")
    private Integer questionType;

    @JsonProperty("answer")
    private PracticeAnswer answer;
}
