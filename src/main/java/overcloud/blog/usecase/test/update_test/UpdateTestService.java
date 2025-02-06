package overcloud.blog.usecase.test.update_test;

import overcloud.blog.response.RestResponse;

public interface UpdateTestService {
    Void updateTest(String id, TestUpdateRequest request);
}
