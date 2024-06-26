package overcloud.blog.usecase.test.get_practices.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.core.datetime.DateTimeService;
import overcloud.blog.entity.PracticeEntity;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.test.common.PracticeResMsg;
import overcloud.blog.usecase.test.common.PracticeResponse;
import overcloud.blog.usecase.test.common.UserPracticeResponse;
import overcloud.blog.usecase.test.get_practices.UserPracticeService;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserPraticeServiceImpl implements UserPracticeService {
    private final IPracticeRepository practiceRepository;
    private final IUserRepository userRepository;
    private final DateTimeService dateTimeService;
    private final ResFactory resFactory;

    UserPraticeServiceImpl(IPracticeRepository practiceRepository,
                           IUserRepository userRepository,
                           DateTimeService dateTimeService,
                           ResFactory resFactory) {
        this.practiceRepository = practiceRepository;
        this.userRepository = userRepository;
        this.dateTimeService = dateTimeService;
        this.resFactory = resFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public RestResponse<UserPracticeResponse> getUserPractice(String username) {
        UserPracticeResponse response = new UserPracticeResponse();
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND));
        }

        List<PracticeEntity> practices = practiceRepository.findByTesterId(user.getId());

        List<PracticeResponse> practiceResponseList = new ArrayList<>();
        for (PracticeEntity practiceEntity : practices) {
            TestEntity test = practiceEntity.getTest();
            PracticeResponse practice = PracticeResponse.builder()
                    .id(practiceEntity.getId().toString())
                    .testTitle(test.getTitle())
                    .date(dateTimeService.dateTimeToString(practiceEntity.getCreatedAt()))
                    .build();
            practiceResponseList.add(practice);
        }
        response.setPractices(practiceResponseList);

        return resFactory.success(PracticeResMsg.PRACTICE_GET_LIST, response);
    }

}
