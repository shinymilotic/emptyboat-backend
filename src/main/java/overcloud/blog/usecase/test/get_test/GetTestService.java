package overcloud.blog.usecase.test.get_test;

import org.springframework.stereotype.Service;

import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.test.common.TestResponse;

@Service
public interface GetTestService {
    RestResponse<TestResponse> getTest(String id);
}
