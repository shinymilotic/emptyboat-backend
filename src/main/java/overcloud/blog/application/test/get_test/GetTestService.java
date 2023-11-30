package overcloud.blog.application.test.get_test;

import org.springframework.stereotype.Service;
import overcloud.blog.application.test.common.TestResponse;

@Service
public interface GetTestService {
    TestResponse getTest(String slug);
}
