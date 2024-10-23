package overcloud.blog.usecase.test.create_practice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PracticeOpenAnswer implements PracticeAnswer {
    @JsonProperty("answer")
    private String answer;
}
