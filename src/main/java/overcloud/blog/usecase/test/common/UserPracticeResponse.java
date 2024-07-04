package overcloud.blog.usecase.test.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserPracticeResponse {
    private List<PracticeResponse> practices;
}
