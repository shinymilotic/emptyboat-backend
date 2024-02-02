package overcloud.blog.usecase.test.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserPracticeResponse {
    @JsonProperty("practices")
    private List<PracticeResponse> practices;
}
