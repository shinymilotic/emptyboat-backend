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
    @JsonProperty("test_id")
    private String testId;

    @JsonProperty("choices")
    private List<String> answerIds;
}
