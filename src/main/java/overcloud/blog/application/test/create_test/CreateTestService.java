package overcloud.blog.application.test.create_test;

import org.springframework.stereotype.Service;
import overcloud.blog.application.test.TestRequest;
import overcloud.blog.application.test.TestResponse;

@Service
public interface CreateTestService {
    String createTest(TestRequest testRequest);
}
