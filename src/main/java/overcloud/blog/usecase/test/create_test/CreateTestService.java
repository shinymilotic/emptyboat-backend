package overcloud.blog.usecase.test.create_test;

import overcloud.blog.response.RestResponse;
import overcloud.blog.usecase.test.create_test.request.TestRequest;

public interface CreateTestService {
    RestResponse<Void> createTest(TestRequest testRequest);
}
