package overcloud.blog.usecase.test.create_test;

import org.springframework.stereotype.Service;

import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.test.common.TestRequest;

public interface CreateTestService {
    RestResponse<Void> createTest(TestRequest testRequest);
}
