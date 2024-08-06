package overcloud.blog.usecase.test.update_test;

import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.entity.TestEntity;
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
        testRepo.updateTest(testId, request.getTitle(), request.getDescription());
        
        return resFactory.success(TestResMsg.TEST_UPDATE_SUCCESS, null);
    }
    
}
