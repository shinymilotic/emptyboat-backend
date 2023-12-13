package overcloud.blog.application.test.create_test;

import org.springframework.stereotype.Service;
import overcloud.blog.application.test.common.TestRequest;

@Service
public interface CreateTestService {
    void createTest(TestRequest testRequest);
}
