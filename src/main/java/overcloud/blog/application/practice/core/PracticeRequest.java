package overcloud.blog.application.practice.core;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@JsonTypeName("practice")
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class PracticeRequest {
    @JsonProperty("slug")
    private String slug;

    @JsonProperty("choiceAnswers")
    private List<String> choiceAnswers;

    @JsonProperty("essayAnswers")
    private List<EssayAnswer> essayAnswers;
}
