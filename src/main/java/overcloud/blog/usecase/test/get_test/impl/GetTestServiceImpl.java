package overcloud.blog.usecase.test.get_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.repository.TestRepository;
import overcloud.blog.usecase.test.common.TestResMsg;
import overcloud.blog.usecase.test.get_test.response.TestResponse;
import overcloud.blog.usecase.test.get_test.GetTestService;
import overcloud.blog.utils.validation.ObjectsValidator;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetTestServiceImpl implements GetTestService {
    private final TestRepository testRepository;
    private final ObjectsValidator validator;

    public GetTestServiceImpl(TestRepository testRepository, ObjectsValidator validator) {
        this.testRepository = testRepository;
        this.validator = validator;
    }

    @Override
    @Transactional
    public TestResponse getTest(String id) {
        Optional<TestResponse> res = this.testRepository.getTestResponse(UUID.fromString(id));

        if (res.isEmpty()) {
            throw validator.fail(TestResMsg.TEST_NOT_FOUND);
        }
        
        return res.get();
    }

}
