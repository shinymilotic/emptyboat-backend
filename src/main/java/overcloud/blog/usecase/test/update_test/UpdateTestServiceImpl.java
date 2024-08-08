package overcloud.blog.usecase.test.update_test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ApiError;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.common.validation.ObjectsValidator;
import overcloud.blog.usecase.test.common.TestResMsg;

public class UpdateTestServiceImpl implements UpdateTestService {
    private final ObjectsValidator validator;
    private final ResFactory resFactory;
    private final ITestRepository testRepo;

    public UpdateTestServiceImpl(ObjectsValidator validator, ResFactory resFactory, ITestRepository testRepo) {
        this.validator = validator;
        this.resFactory = resFactory;
        this.testRepo = testRepo;
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
        
        for (UpdQuestion question : questions) {


        }

        for (UpdQuestion question : questions) {
            if (question.getQuestionType() == 1) {
                UpdChoiceQuestion choiceQuestion = (UpdChoiceQuestion) question;
            } else if (question.getQuestionType() == 2) {
                UpdEssayQuestion essayQuestion = (UpdEssayQuestion) question;

            }

            if (question.getUpdateFlg() == UpdateFlg.CREATE.getValue()) {
                

            } else if (question.getUpdateFlg() == UpdateFlg.UPDATE.getValue()) {

            } else if (question.getUpdateFlg() == UpdateFlg.DELETE.getValue()) {

            }
        }
        return resFactory.success(TestResMsg.TEST_UPDATE_SUCCESS, null);
    }
    
}
