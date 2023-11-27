package overcloud.blog.application.test.create_test;

import org.springframework.stereotype.Service;
import overcloud.blog.application.test.common.TestRequest;

@Service
public interface CreateTestService {
    String createTest(TestRequest testRequest);
}
