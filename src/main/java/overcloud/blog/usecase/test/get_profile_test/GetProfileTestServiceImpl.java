package overcloud.blog.usecase.test.get_profile_test;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.usecase.test.common.TestResMsg;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetProfileTestServiceImpl implements GetProfileTestService {
    private final ITestRepository testRepository;
    private final IUserRepository userRepository;
    private final ObjectsValidator validator;

    public GetProfileTestServiceImpl(ITestRepository testRepository,
                                 IUserRepository userRepository,
                                 ObjectsValidator validator) {
        this.testRepository = testRepository;
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @Override
    @Transactional
    public List<ProfileTestRes> getProfileTests(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            throw validator.fail(UserResMsg.USER_NOT_FOUND);
        }

        List<TestEntity> tests = testRepository.getProfileTest(userEntity.getUserId());

        if (tests.isEmpty()) {
            throw validator.fail(TestResMsg.TEST_NOT_FOUND);
        }

        List<ProfileTestRes> profileTests = new ArrayList<>();

        tests.forEach((test) -> {
            ProfileTestRes profileTest = new ProfileTestRes();
            profileTest.setId(test.getTestId().toString());
            profileTest.setTitle(test.getTitle());
            profileTest.setDescription(test.getDescription());
            profileTests.add(profileTest);
        });

        return profileTests;
    }
}
