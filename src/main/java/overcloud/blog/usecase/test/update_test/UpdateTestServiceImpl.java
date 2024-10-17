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
import overcloud.blog.entity.AnswerEntity;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.repository.IChoiceAnswerRepository;
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
    private final IPracticeOpenAnswerRepository openAnswerRepo;

    public UpdateTestServiceImpl(ObjectsValidator validator,
            ResFactory resFactory,
            ITestRepository testRepo,
            IQuestionRepository questionRepo,
            IChoiceAnswerRepository choiceAnswerRepo,
            ITestQuestionRepository testQuestionRepo,
            IPracticeOpenAnswerRepository openAnswerRepo) {
        this.validator = validator;
        this.resFactory = resFactory;
        this.testRepo = testRepo;
        this.questionRepo = questionRepo;
        this.choiceAnswerRepo = choiceAnswerRepo;
        this.testQuestionRepo = testQuestionRepo;
        this.openAnswerRepo = openAnswerRepo;
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
        testRepo.updateTest(testId, request.getTitle(), request.getDescription());
        List<QuestionEntity> insertList = new ArrayList<>();
        List<QuestionEntity> updateList = new ArrayList<>();
        List<UUID> deleteQuestionList = new ArrayList<>();
        List<AnswerEntity> insertAnswers = new ArrayList<>();
        List<AnswerEntity> updateAnswers = new ArrayList<>();
        List<UUID> deleteAnswers = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (UpdQuestion question : questions) {
            if (question.getUpdateFlg().equals(UpdateFlg.NEW.getValue())) {
                QuestionEntity questionEntity = new QuestionEntity();
                questionEntity.setQuestionId(UuidCreator.getTimeOrderedEpoch());
                questionEntity.setQuestion(question.getQuestion());
                questionEntity.setQuestionType(question.getQuestionType());
                questionEntity.setCreatedAt(now);
                questionEntity.setUpdatedAt(now);
                insertList.add(questionEntity);

                if (question.getQuestionType().equals(QuestionType.CHOICE.getValue())) {
                    UpdChoiceQuestion choiceQuestion = (UpdChoiceQuestion) question;
                    List<UpdChoiceAnswer> answers = choiceQuestion.getAnswers();

                    for (UpdChoiceAnswer answer : answers) {
                        if (answer.getUpdateFlg().equals(UpdateFlg.NEW.getValue())) {
                            AnswerEntity answerEntity = new AnswerEntity();
                            answerEntity.setChoiceAnswerId(UuidCreator.getTimeOrderedEpoch());
                            answerEntity.setAnswer(answer.getAnswer());
                            answerEntity.setTruth(answer.getTruth());
                            answerEntity.setQuestionId(null);
                            answerEntity.setCreatedAt(now);
                            answerEntity.setUpdatedAt(now);
                            insertAnswers.add(answerEntity);
                        } else if (answer.getUpdateFlg().equals(UpdateFlg.UPDATE.getValue())) {
                            AnswerEntity answerEntity = new AnswerEntity();
                            answerEntity.setChoiceAnswerId(UuidCreator.getTimeOrderedEpoch());
                            answerEntity.setAnswer(answer.getAnswer());
                            answerEntity.setTruth(answer.getTruth());
                            answerEntity.setCreatedAt(now);
                            answerEntity.setUpdatedAt(now);
                            updateAnswers.add(answerEntity);
                        } else if (answer.getUpdateFlg().equals(UpdateFlg.DELETE.getValue())) {
                            deleteAnswers.add(UUID.fromString(answer.getAnswerId()));
                        }
                    }
                }
            } else if (question.getUpdateFlg().equals(UpdateFlg.UPDATE.getValue())) {
                QuestionEntity questionEntity = new QuestionEntity();
                questionEntity.setQuestionId(UUID.fromString(question.getId()));
                questionEntity.setQuestion(question.getQuestion());
                questionEntity.setQuestionType(question.getQuestionType());
                questionEntity.setUpdatedAt(now);
                updateList.add(questionEntity);
            } else if (question.getUpdateFlg().equals(UpdateFlg.DELETE.getValue())) {
                deleteQuestionList.add(UUID.fromString(question.getId()));
            }
        }

        questionRepo.saveAll(insertList);
        questionRepo.updateAll(updateList);
        openAnswerRepo.deleteAllByQuestionId(deleteQuestionList);
        testQuestionRepo.deleteAllById(deleteQuestionList);
        questionRepo.deleteAll(deleteQuestionList);
        choiceAnswerRepo.saveAll(insertAnswers);
        choiceAnswerRepo.updateAll(updateAnswers);
        choiceAnswerRepo.deleteAll(deleteAnswers);
        
        return resFactory.success(TestResMsg.TEST_UPDATE_SUCCESS, null);
    }
    
}
