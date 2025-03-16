package overcloud.blog.usecase.test.get_practices.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.Tuple;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.utils.datetime.DateTimeService;
import overcloud.blog.repository.PracticeRepository;
import overcloud.blog.repository.UserRepository;
import overcloud.blog.usecase.test.get_practices.response.PracticeResponse;
import overcloud.blog.usecase.test.get_practices.UserPracticeService;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserPraticeServiceImpl implements UserPracticeService {
    private final PracticeRepository practiceRepository;
    private final UserRepository userRepository;
    private final DateTimeService dateTimeService;
    private final ObjectsValidator validator;

    UserPraticeServiceImpl(PracticeRepository practiceRepository,
                           UserRepository userRepository,
                           DateTimeService dateTimeService,
                           ObjectsValidator validator) {
        this.practiceRepository = practiceRepository;
        this.userRepository = userRepository;
        this.dateTimeService = dateTimeService;
        this.validator = validator;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PracticeResponse> getUserPractice(String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw validator.fail(UserResMsg.USER_NOT_FOUND);
        }

        List<Tuple> data = practiceRepository.findByTesterId(user.getUserId());

        List<PracticeResponse> practiceResponseList = new ArrayList<>();
        for (Tuple row : data) {
            UUID practiceId = (UUID) row.get(0);
            String testTitle = (String) row.get(1);
            LocalDateTime createdAt = (LocalDateTime) row.get(2);

            PracticeResponse practice = PracticeResponse.builder()
                    .id(practiceId.toString())
                    .testTitle(testTitle)
                    .date(dateTimeService.dateTimeToString(createdAt))
                    .build();
            practiceResponseList.add(practice);
        }

        return practiceResponseList;
    }

}
