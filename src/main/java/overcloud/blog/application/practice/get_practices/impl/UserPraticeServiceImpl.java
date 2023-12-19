package overcloud.blog.application.practice.get_practices.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import overcloud.blog.application.practice.core.PracticeResponse;
import overcloud.blog.application.practice.core.UserPracticeResponse;
import overcloud.blog.application.practice.get_practices.UserPracticeService;
import overcloud.blog.application.user.core.UserError;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.datetime.DateTimeService;
import overcloud.blog.entity.PracticeEntity;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.repository.PracticeRepository;

@Service
public class UserPraticeServiceImpl implements UserPracticeService{

    private final PracticeRepository practiceRepository;
    private final UserRepository userRepository;
    private final DateTimeService dateTimeService;

    UserPraticeServiceImpl(PracticeRepository practiceRepository,
        UserRepository userRepository,
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
            // throw
            throw new InvalidDataException(UserError.USER_NOT_FOUND);
        }

        List<PracticeEntity> practices = practiceRepository.findByTesterId(user.getId());

        List<PracticeResponse> practiceResponseList = new ArrayList<>();
        for (PracticeEntity practiceEntity : practices) {
            TestEntity test = practiceEntity.getTest();
            PracticeResponse practice = PracticeResponse.builder()
                .testTitle(test.getTitle())
                .date(dateTimeService.dateTimeToString(practiceEntity.getCreatedAt()))
                .score(practiceEntity.getScore()).build();
            practiceResponseList.add(practice);
        }
        response.setPractices(practiceResponseList);

        return response;
    }
    
}
