package overcloud.blog.usecase.test.get_practice;

import java.util.UUID;

import overcloud.blog.response.RestResponse;

public interface GetPracticeResultService {
    RestResponse<PracticeResult> getPracticeResult(UUID practiceId);
}
