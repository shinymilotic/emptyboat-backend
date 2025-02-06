package overcloud.blog.usecase.test.get_test;

import org.springframework.stereotype.Service;
import overcloud.blog.usecase.test.get_test.response.TestResponse;

@Service
public interface GetTestService {
    TestResponse getTest(String id);
}
