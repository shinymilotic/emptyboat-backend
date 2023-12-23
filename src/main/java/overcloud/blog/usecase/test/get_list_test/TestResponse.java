package overcloud.blog.usecase.test.get_list_test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TestResponse {
    @JsonProperty("title")
    private String title;

    @JsonProperty("slug")
    private String slug;
}
