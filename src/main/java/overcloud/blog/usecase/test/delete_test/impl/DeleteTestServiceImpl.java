package overcloud.blog.usecase.test.delete_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.test.common.TestResMsg;
import overcloud.blog.usecase.test.delete_test.DeleteTestService;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeleteTestServiceImpl implements DeleteTestService {
    private final ITestRepository testRepository;
    private final IPracticeRepository practiceRepository;
    private final ResFactory resFactory ;

    DeleteTestServiceImpl(
            ITestRepository testRepository,
            IPracticeRepository practiceRepository,
            ResFactory resFactory) {
        this.testRepository = testRepository;
        this.practiceRepository = practiceRepository;
        this.resFactory = resFactory;
    }

    @Override
    @Transactional
    public RestResponse<Void> deleteTest(String id) {
        Optional<TestEntity> test = testRepository.findById(UUID.fromString(id));

        if (!test.isPresent()) {
            throw new InvalidDataException(resFactory.fail(TestResMsg.TEST_NOT_FOUND));
        }

        practiceRepository.deleteByTestId(test.get().getId());
        testRepository.deleteById(test.get().getId());

        return resFactory.success(TestResMsg.TEST_CREATE_SUCCESS, null);
    }

}
