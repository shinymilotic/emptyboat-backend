package overcloud.blog.application.practice.core;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PracticeResponse {
    @JsonProperty("testTitle")
    private String testTitle;

    @JsonProperty("date")
    private LocalDateTime date;

    @JsonProperty("score")
    private int score;
}
