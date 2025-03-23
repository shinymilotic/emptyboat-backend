package overcloud.blog.usecase.test.delete_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.repository.*;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.usecase.test.common.TestResMsg;
import overcloud.blog.usecase.test.delete_test.DeleteTestService;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeleteTestServiceImpl implements DeleteTestService {
    private final TestRepository testRepository;
    private final PracticeRepository practiceRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceAnswerRepository choiceAnswerRepository;
    private final PracticeChoiceRepository practiceChoiceRepository;
    private final PracticeOpenAnswerRepository practiceOpenAnswerRepository;
    private final ObjectsValidator validator ;
    private final SpringAuthenticationService authenticationService;

    DeleteTestServiceImpl(
            TestRepository testRepository,
            PracticeRepository practiceRepository,
            QuestionRepository questionRepository,
            ChoiceAnswerRepository choiceAnswerRepository,
            PracticeChoiceRepository practiceChoiceRepository,
            PracticeOpenAnswerRepository practiceOpenAnswerRepository,
            ObjectsValidator validator,
            SpringAuthenticationService authenticationService) {
        this.testRepository = testRepository;
        this.practiceRepository = practiceRepository;
        this.questionRepository = questionRepository;
        this.choiceAnswerRepository = choiceAnswerRepository;
        this.practiceChoiceRepository = practiceChoiceRepository;
        this.practiceOpenAnswerRepository = practiceOpenAnswerRepository;
        this.validator = validator;
        this.authenticationService = authenticationService;
    }

    @Override
    @Transactional
    public Void deleteTest(String id) {
        Optional<TestEntity> test = testRepository.findById(UUID.fromString(id));
        UserEntity currentUser = authenticationService.getCurrentUser()
                        .orElseThrow(() -> validator.fail(UserResMsg.USER_NOT_FOUND))
                        .getUser();
        
        if (test.isEmpty()) {
            throw validator.fail(TestResMsg.TEST_NOT_FOUND);
        }

        if (!currentUser.getUserId().equals(test.get().getAuthorId())) {
            throw validator.fail(TestResMsg.TEST_AUTHOR_NOT_MATCH);
        }

        UUID testId = test.get().getTestId();

        List<UUID> practiceIdList = practiceRepository.findByTestId(testId);
        List<UUID> questionIdList = questionRepository.findByTestId(testId);

        if (!practiceIdList.isEmpty()) {
            practiceChoiceRepository.deleteByPracticeIdList(practiceIdList);
            practiceOpenAnswerRepository.deleteByPracticeIdList(practiceIdList);
        }

        if (!questionIdList.isEmpty()) {
            choiceAnswerRepository.deleteByQuestionIdList(questionIdList);
        }

        practiceRepository.deleteByTestId(testId);
        questionRepository.deleteByTestId(testId);
        testRepository.deleteById(testId);

        return null;
    }

}
