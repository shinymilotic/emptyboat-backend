package overcloud.blog.usecase.test.delete_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ResFactory;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.repository.ITestQuestionRepository;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.test.common.TestResMsg;
import overcloud.blog.usecase.test.delete_test.DeleteTestService;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeleteTestServiceImpl implements DeleteTestService {
    private final ITestRepository testRepository;
    private final IPracticeRepository practiceRepository;
    private final ITestQuestionRepository testQuestionRepository;
    private final ResFactory resFactory ;
    private final SpringAuthenticationService authenticationService;

    DeleteTestServiceImpl(
            ITestRepository testRepository,
            IPracticeRepository practiceRepository,
            ResFactory resFactory,
            ITestQuestionRepository testQuestionRepository,
            SpringAuthenticationService authenticationService) {
        this.testRepository = testRepository;
        this.practiceRepository = practiceRepository;
        this.resFactory = resFactory;
        this.authenticationService = authenticationService;
        this.testQuestionRepository = testQuestionRepository;
    }

    @Override
    @Transactional
    public Void deleteTest(String id) {
        Optional<TestEntity> test = testRepository.findById(UUID.fromString(id));
        UserEntity currentUser = authenticationService.getCurrentUser()
                        .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                        .getUser();
        
        if (test.isEmpty()) {
            throw new InvalidDataException(resFactory.fail(TestResMsg.TEST_NOT_FOUND));
        }

        if (!currentUser.getUserId().equals(test.get().getAuthorId())) {
            throw new InvalidDataException(resFactory.fail(TestResMsg.TEST_AUTHOR_NOT_MATCH));
        }

        UUID testId = test.get().getTestId();
        practiceRepository.deleteTestId(testId);
        testQuestionRepository.deleteByTestId(testId);
        testRepository.deleteById(testId);

        return null;
    }

}
