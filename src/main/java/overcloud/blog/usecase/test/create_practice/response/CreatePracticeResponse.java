package overcloud.blog.usecase.test.create_practice.response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePracticeResponse {
    @JsonProperty("practiceId")
    private UUID practiceId;
}
