package overcloud.blog.usecase.test.update_test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.f4b6a3.uuid.UuidCreator;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ApiError;
import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
import overcloud.blog.utils.validation.ObjectsValidator;
import overcloud.blog.entity.ChoiceAnswerEntity;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.entity.TestQuestion;
import overcloud.blog.entity.TestQuestionId;
import overcloud.blog.repository.IChoiceAnswerRepository;
import overcloud.blog.repository.IPracticeChoiceRepository;
import overcloud.blog.repository.IPracticeOpenAnswerRepository;
import overcloud.blog.repository.IQuestionRepository;
import overcloud.blog.repository.ITestQuestionRepository;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.test.common.QuestionType;
import overcloud.blog.usecase.test.common.TestResMsg;
import overcloud.blog.usecase.user.common.UpdateFlg;

@Service
public class UpdateTestServiceImpl implements UpdateTestService {
    private final ObjectsValidator validator;
    private final ResFactory resFactory;
    private final ITestRepository testRepo;
    private final IQuestionRepository questionRepo;
    private final IChoiceAnswerRepository choiceAnswerRepo;
    private final ITestQuestionRepository testQuestionRepo;
    private final IPracticeOpenAnswerRepository practiceOpenAnswerRepo;
    private final IPracticeChoiceRepository practiceChoiceRepo;

    public UpdateTestServiceImpl(ObjectsValidator validator,
            ResFactory resFactory,
            ITestRepository testRepo,
            IQuestionRepository questionRepo,
            IChoiceAnswerRepository choiceAnswerRepo,
            ITestQuestionRepository testQuestionRepo,
            IPracticeOpenAnswerRepository practiceOpenAnswerRepo,
            IPracticeChoiceRepository practiceChoiceRepo) {
        this.validator = validator;
        this.resFactory = resFactory;
        this.testRepo = testRepo;
        this.questionRepo = questionRepo;
        this.choiceAnswerRepo = choiceAnswerRepo;
        this.testQuestionRepo = testQuestionRepo;
        this.practiceOpenAnswerRepo = practiceOpenAnswerRepo;
        this.practiceChoiceRepo = practiceChoiceRepo;
    }

    @Override
    @Transactional
    public RestResponse<Void> updateTest(String id, TestUpdateRequest request) {
        UUID testId = UUID.fromString(id);
        Optional<ApiError> apiError = validator.validate(request);
        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }
        List<UpdQuestion> questions = request.getQuestions();
        List<QuestionEntity> questionsToInsert = new ArrayList<>();
        List<QuestionEntity> questionsToUpdate = new ArrayList<>();
        List<UUID> questionsToDelete = new ArrayList<>();
        List<ChoiceAnswerEntity> answersToInsert = new ArrayList<>();
        List<ChoiceAnswerEntity> answersToUpdate = new ArrayList<>();
        List<UUID> answersToDelete = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (UpdQuestion question : questions) {
            if (question.getUpdateFlg().equals(UpdateFlg.NEW.getValue())) {
                UUID questionId = UuidCreator.getTimeOrderedEpoch();
                questionsToInsert.add(QuestionEntity.builder()
                        .questionId(questionId)
                        .question(question.getQuestion())
                        .questionType(question.getQuestionType())
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
                );

                if (question.getQuestionType().equals(QuestionType.CHOICE.getValue())) {
                    ((UpdChoiceQuestion) question).getAnswers().stream()
                        .forEach(answer -> answersToInsert.add(ChoiceAnswerEntity.builder()
                            .choiceAnswerId(UuidCreator.getTimeOrderedEpoch())
                            .answer(answer.getAnswer())
                            .truth(answer.getTruth())
                            .questionId(questionId)
                            .createdAt(now)
                            .updatedAt(now).build()));
                }
            } else if (question.getUpdateFlg().equals(UpdateFlg.UPDATE.getValue())) {
                UUID questionId = UUID.fromString(question.getId());
                questionsToUpdate.add(QuestionEntity.builder()
                        .questionId(questionId)
                        .question(question.getQuestion())
                        .questionType(question.getQuestionType())
                        .updatedAt(now).build()
                );

                if (question.getQuestionType().equals(QuestionType.CHOICE.getValue())) {
                    ((UpdChoiceQuestion) question).getAnswers().stream()
                        .forEach(answer -> 
                            answersToUpdate.add(ChoiceAnswerEntity.builder()
                            .choiceAnswerId(UUID.fromString(answer.getId()))
                            .answer(answer.getAnswer())
                            .truth(answer.getTruth())
                            .questionId(questionId)
                            .createdAt(now)
                            .updatedAt(now).build()
                        ));
                }
            } else if (question.getUpdateFlg().equals(UpdateFlg.DELETE.getValue())) {
                questionsToDelete.add(UUID.fromString(question.getId()));

                if (question.getQuestionType().equals(QuestionType.CHOICE.getValue())) {
                    ((UpdChoiceQuestion) question).getAnswers().stream()
                        .forEach(answer -> answersToDelete.add(UUID.fromString(answer.getId())));
                }
            }
        }

        testRepo.updateTest(testId, request.getTitle(), request.getDescription());
        testQuestionRepo.deleteAllById(questionsToDelete);
        choiceAnswerRepo.deleteAll(answersToDelete);
        practiceOpenAnswerRepo.deleteAllByQuestionId(questionsToDelete);
        practiceChoiceRepo.deleteAll(answersToDelete);
        questionRepo.deleteAll(questionsToDelete);
        questionRepo.saveAll(questionsToInsert);
        testQuestionRepo.saveAll(testQuestions(questionsToInsert, testId));
        choiceAnswerRepo.saveAll(answersToInsert);
        questionRepo.updateAll(questionsToUpdate);
        choiceAnswerRepo.updateAll(answersToUpdate);
        
        return resFactory.success(TestResMsg.TEST_UPDATE_SUCCESS, null);
    }

    public List<TestQuestion> testQuestions(List<QuestionEntity> questions, UUID testId) {
        List<TestQuestion> testQuestions = new ArrayList<>();
        for (QuestionEntity question : questions) {
            TestQuestionId id = new TestQuestionId();
            id.setTestId(testId);
            id.setQuestionId(question.getQuestionId());
            testQuestions.add(new TestQuestion(id));
        }

        return testQuestions;
    }
    
}
