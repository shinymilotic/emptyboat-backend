package overcloud.blog.usecase.test.get_profile_test;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.stylesheets.LinkStyle;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
import overcloud.blog.usecase.test.common.TestResMsg;
import overcloud.blog.usecase.user.common.UserResMsg;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetProfileTestServiceImpl implements GetProfileTestService {
    private final ITestRepository testRepository;
    private final IUserRepository userRepository;
    private final ResFactory resFactory;

    public GetProfileTestServiceImpl(ITestRepository testRepository,
                                 IUserRepository userRepository,
                                 ResFactory resFactory) {
        this.testRepository = testRepository;
        this.userRepository = userRepository;
        this.resFactory = resFactory;
    }

    @Override
    @Transactional
    public RestResponse<List<ProfileTestRes>> getProfileTests(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            throw new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND));
        }

        List<TestEntity> tests = testRepository.getProfileTest(userEntity.getUserId());

        if (tests.isEmpty()) {
            throw new InvalidDataException(resFactory.fail(TestResMsg.TEST_NOT_FOUND));
        }

        List<ProfileTestRes> profileTests = new ArrayList<>();

        tests.forEach((test) -> {
            ProfileTestRes profileTest = new ProfileTestRes();
            profileTest.setId(test.getTestId().toString());
            profileTest.setTitle(test.getTitle());
            profileTest.setDescription(test.getDescription());
            profileTests.add(profileTest);
        });

        return resFactory.success(TestResMsg.TEST_PROFILE_GET_SUCCESS, profileTests);
    }
}
