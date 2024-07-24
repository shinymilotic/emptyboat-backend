package overcloud.blog.usecase.test.delete_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.test.common.TestResMsg;
import overcloud.blog.usecase.test.delete_test.DeleteTestService;
import overcloud.blog.usecase.user.common.UserResMsg;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeleteTestServiceImpl implements DeleteTestService {
    private final ITestRepository testRepository;
    private final IPracticeRepository practiceRepository;
    private final ResFactory resFactory ;
    private final SpringAuthenticationService authenticationService;

    DeleteTestServiceImpl(
            ITestRepository testRepository,
            IPracticeRepository practiceRepository,
            ResFactory resFactory,
            SpringAuthenticationService authenticationService) {
        this.testRepository = testRepository;
        this.practiceRepository = practiceRepository;
        this.resFactory = resFactory;
        this.authenticationService = authenticationService;
    }

    @Override
    @Transactional
    public RestResponse<Void> deleteTest(String id) {
        Optional<TestEntity> test = testRepository.findById(UUID.fromString(id));
        UserEntity currentUser = authenticationService.getCurrentUser()
                        .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                        .getUser();
        
        if (!test.isPresent()) {
            throw new InvalidDataException(resFactory.fail(TestResMsg.TEST_NOT_FOUND));
        }

        if (!currentUser.getId().equals(test.get().getAuthorId())) {
            throw new InvalidDataException(resFactory.fail(TestResMsg.TEST_AUTHOR_NOT_MATCH));
        }

        practiceRepository.deleteByTestId(test.get().getId());
        testRepository.deleteById(test.get().getId());

        return resFactory.success(TestResMsg.TEST_CREATE_SUCCESS, null);
    }

}
