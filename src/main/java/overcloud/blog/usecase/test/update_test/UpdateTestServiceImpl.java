package overcloud.blog.usecase.test.update_test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

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
        List<UpdQuestion> insertList = new ArrayList<>();
        List<UpdQuestion> updateList = new ArrayList<>();
        List<UpdQuestion> deleteList = new ArrayList<>();
        List<UpdChoiceAnswer> insertAnswers = new ArrayList<>();
        List<UpdChoiceAnswer> updateAnswers = new ArrayList<>();
        List<UpdChoiceAnswer> deleteAnswers = new ArrayList<>();
        for (UpdQuestion question : questions) {
            if (question.getUpdateFlg().equals(UpdateFlg.NEW.getValue())) {
                question.getQuestion();
                question.getQuestionType();
                insertList.add(question);
            } else if (question.getUpdateFlg().equals(UpdateFlg.UPDATE.getValue())) {
                updateList.add(question);
            } else if (question.getUpdateFlg().equals(UpdateFlg.DELETE.getValue())) {
                deleteList.add(question);
            }

            if (question.getQuestionType().equals(1) && question.getUpdateFlg().equals(UpdateFlg.DELETE.getValue())) {
                UpdChoiceQuestion choiceQuestion = (UpdChoiceQuestion) question;
                List<UpdChoiceAnswer> answers = choiceQuestion.getAnswers();
                
                for (UpdChoiceAnswer answer : answers) {
                    if (answer.getUpdateFlg().equals(UpdateFlg.NEW.getValue())) {
                        insertAnswers.add(answer);
                    } else if (answer.getUpdateFlg().equals(UpdateFlg.UPDATE.getValue())) {
                        updateAnswers.add(answer);
                    } else if (answer.getUpdateFlg().equals(UpdateFlg.DELETE.getValue())) {
                        deleteAnswers.add(answer);
                    }
                }
                // insertList.add(choiceQuestion);
            }
        }
        questionRepo.saveAll(insertList);
        questionRepo.updateAll(updateList);
        questionRepo.deleteAll(deleteList);
        choiceAnswerRepo.saveAll(null);
        choiceAnswerRepo.updateAll(null);
        choiceAnswerRepo.deleteAll(null);
        
        return resFactory.success(TestResMsg.TEST_UPDATE_SUCCESS, null);
    }
    
}
