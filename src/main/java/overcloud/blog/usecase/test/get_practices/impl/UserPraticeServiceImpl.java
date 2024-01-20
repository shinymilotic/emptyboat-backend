package overcloud.blog.usecase.test.get_practices.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import overcloud.blog.entity.PracticeEntity;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.infrastructure.datetime.DateTimeService;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.usecase.test.common.PracticeResponse;
import overcloud.blog.usecase.test.common.UserPracticeResponse;
import overcloud.blog.usecase.test.get_practices.UserPracticeService;
import overcloud.blog.usecase.auth.common.UserError;

@Service
public class UserPraticeServiceImpl implements UserPracticeService {

    private final IPracticeRepository practiceRepository;
    private final IUserRepository userRepository;
    private final DateTimeService dateTimeService;

    UserPraticeServiceImpl(IPracticeRepository practiceRepository,
                           IUserRepository userRepository,
                           DateTimeService dateTimeService) {
        this.practiceRepository = practiceRepository;
        this.userRepository = userRepository;
        this.dateTimeService = dateTimeService;
    }

    @Override
    public UserPracticeResponse getUserPractice(String username) {
        UserPracticeResponse response = new UserPracticeResponse();
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new InvalidDataException(UserError.USER_NOT_FOUND);
        }

        List<PracticeEntity> practices = practiceRepository.findByTesterId(user.getId());

        List<PracticeResponse> practiceResponseList = new ArrayList<>();
        for (PracticeEntity practiceEntity : practices) {
            TestEntity test = practiceEntity.getTest();
            PracticeResponse practice = PracticeResponse.builder()
                .id(practiceEntity.getId().toString())
                .testTitle(test.getTitle())
                .date(dateTimeService.dateTimeToString(practiceEntity.getCreatedAt()))
                .score(practiceEntity.getScore()).build();
            practiceResponseList.add(practice);
        }
        response.setPractices(practiceResponseList);

        return response;
    }
    
}
