package overcloud.blog.usecase.test.get_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ResFactory;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.test.common.TestResMsg;
import overcloud.blog.usecase.test.get_test.response.TestResponse;
import overcloud.blog.usecase.test.get_test.GetTestService;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetTestServiceImpl implements GetTestService {
    private final ITestRepository testRepository;
    private final ResFactory resFactory;

    public GetTestServiceImpl(ITestRepository testRepository, ResFactory resFactory) {
        this.testRepository = testRepository;
        this.resFactory = resFactory;
    }

    @Override
    @Transactional
    public TestResponse getTest(String id) {
        Optional<TestResponse> res = this.testRepository.getTestResponse(UUID.fromString(id));

        if (res.isEmpty()) {
            throw resFactory.fail(TestResMsg.TEST_NOT_FOUND);
        }
        
        return res.get();
    }

}
