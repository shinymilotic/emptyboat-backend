package overcloud.blog.usecase.test.get_list_test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TestListResponse {
    @JsonProperty("tests")
    private List<TestResponse> tests;
}
