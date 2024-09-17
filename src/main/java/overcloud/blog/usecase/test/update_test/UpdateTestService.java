package overcloud.blog.usecase.test.update_test;

import overcloud.blog.response.RestResponse;

public interface UpdateTestService {
    RestResponse<Void> updateTest(String id, TestUpdateRequest request);
}
