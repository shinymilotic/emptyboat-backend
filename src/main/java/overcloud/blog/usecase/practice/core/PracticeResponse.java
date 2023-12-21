package overcloud.blog.usecase.practice.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PracticeResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("testTitle")
    private String testTitle;

    @JsonProperty("date")
    private String date;

    @JsonProperty("score")
    private int score;
}
