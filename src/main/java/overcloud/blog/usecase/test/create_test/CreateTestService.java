package overcloud.blog.usecase.test.create_test;

import org.springframework.stereotype.Service;
import overcloud.blog.usecase.test.common.TestRequest;

@Service
public interface CreateTestService {
    void createTest(TestRequest testRequest);
}
