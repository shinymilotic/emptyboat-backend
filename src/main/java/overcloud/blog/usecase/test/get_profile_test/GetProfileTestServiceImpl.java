package overcloud.blog.usecase.test.get_profile_test;

import org.springframework.stereotype.Service;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
import overcloud.blog.usecase.test.common.TestResMsg;
import overcloud.blog.usecase.user.common.UserResMsg;
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
    public RestResponse<ProfileTestRes> getProfileTests(String username) {

        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            throw new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND));
        }

        Optional<TestEntity> testEntityOptional = testRepository.getProfileTest(userEntity.getUserId());

        if (testEntityOptional.isEmpty()) {
            throw new InvalidDataException(resFactory.fail(TestResMsg.TEST_NOT_FOUND));
        }

        TestEntity testEntity = testEntityOptional.get();
        ProfileTestRes res = new ProfileTestRes();
        res.setId(testEntity.getTestId().toString());
        res.setTitle(testEntity.getTitle());
        res.setDescription(testEntity.getDescription());

        return resFactory.success(TestResMsg.TEST_PROFILE_GET_SUCCESS, res);
    }
}
