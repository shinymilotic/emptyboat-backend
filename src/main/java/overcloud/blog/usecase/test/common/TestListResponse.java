package overcloud.blog.usecase.test.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TestListResponse {
    @JsonProperty("tests")
    private List<TestResponse> tests;

    public static TestListResponse testListResponseFactory(List<TestResponse> tests) {
        return TestListResponse.builder()
                .tests(tests).build();
    }
}
