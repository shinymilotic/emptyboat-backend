package overcloud.blog.usecase.test.get_list_test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TestListResponse {
    @JsonProperty("tests")
    private List<SimpleTestResponse> tests;
}
