package overcloud.blog.usecase.test.create_test;

import overcloud.blog.usecase.test.create_test.request.TestRequest;

public interface CreateTestService {
    Void createTest(TestRequest testRequest);
}
