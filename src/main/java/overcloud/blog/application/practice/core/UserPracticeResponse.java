package overcloud.blog.application.practice.core;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserPracticeResponse {
    @JsonProperty("practices")
    private List<PracticeResponse> practices;
}
