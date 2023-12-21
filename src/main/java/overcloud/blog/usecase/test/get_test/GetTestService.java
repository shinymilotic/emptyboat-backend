package overcloud.blog.usecase.test.get_test;

import org.springframework.stereotype.Service;

import overcloud.blog.usecase.test.common.TestResponse;

@Service
public interface GetTestService {
    TestResponse getTest(String slug);
}
