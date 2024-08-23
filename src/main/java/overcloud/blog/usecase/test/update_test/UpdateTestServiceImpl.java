package overcloud.blog.usecase.test.update_test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.f4b6a3.uuid.UuidCreator;
import overcloud.blog.entity.AnswerEntity;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.repository.IChoiceAnswerRepository;
import overcloud.blog.repository.IQuestionRepository;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ApiError;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.common.validation.ObjectsValidator;
import overcloud.blog.usecase.test.common.TestResMsg;
import overcloud.blog.usecase.user.common.UpdateFlg;

@Service
public class UpdateTestServiceImpl implements UpdateTestService {
    private final ObjectsValidator validator;
    private final ResFactory resFactory;
    private final ITestRepository testRepo;
    private final IQuestionRepository questionRepo;
    private final IChoiceAnswerRepository choiceAnswerRepo;

    public UpdateTestServiceImpl(ObjectsValidator validator,
            ResFactory resFactory,
            ITestRepository testRepo,
            IQuestionRepository questionRepo,
            IChoiceAnswerRepository choiceAnswerRepo) {
        this.validator = validator;
        this.resFactory = resFactory;
        this.testRepo = testRepo;
        this.questionRepo = questionRepo;
        this.choiceAnswerRepo = choiceAnswerRepo;
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
        List<UUID> deleteList = new ArrayList<>();
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
            } else if (question.getUpdateFlg().equals(UpdateFlg.UPDATE.getValue())) {
                QuestionEntity questionEntity = new QuestionEntity();
                questionEntity.setQuestionId(UUID.fromString(question.getId()));
                questionEntity.setQuestion(question.getQuestion());
                questionEntity.setQuestionType(question.getQuestionType());
                questionEntity.setUpdatedAt(now);
                updateList.add(questionEntity);
            } else if (question.getUpdateFlg().equals(UpdateFlg.DELETE.getValue())) {
                deleteList.add(UUID.fromString(question.getId()));
            }

            if (question.getQuestionType().equals(1) && question.getUpdateFlg().equals(UpdateFlg.DELETE.getValue())) {
                UpdChoiceQuestion choiceQuestion = (UpdChoiceQuestion) question;
                List<UpdChoiceAnswer> answers = choiceQuestion.getAnswers();
                
                for (UpdChoiceAnswer answer : answers) {
                    if (answer.getUpdateFlg().equals(UpdateFlg.NEW.getValue())) {
                        AnswerEntity answerEntity = new AnswerEntity();
                        answerEntity.setChoiceAnswerId(UuidCreator.getTimeOrderedEpoch());
                        answerEntity.setAnswer(answer.getAnswer());
                        answerEntity.setTruth(answer.getTruth());
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
        }

        questionRepo.saveAll(insertList);
        questionRepo.updateAll(updateList);
        questionRepo.deleteAll(deleteList);
        choiceAnswerRepo.saveAll(insertAnswers);
        choiceAnswerRepo.updateAll(updateAnswers);
        choiceAnswerRepo.deleteAll(deleteAnswers);
        
        return resFactory.success(TestResMsg.TEST_UPDATE_SUCCESS, null);
    }
    
}
