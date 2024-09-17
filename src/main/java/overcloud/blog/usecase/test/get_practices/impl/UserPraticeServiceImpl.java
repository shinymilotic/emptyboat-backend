package overcloud.blog.usecase.test.get_practices.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.Tuple;
import overcloud.blog.common.exceptionhandling.InvalidDataException;
import overcloud.blog.common.response.ResFactory;
import overcloud.blog.common.response.RestResponse;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.datetime.DateTimeService;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.usecase.test.common.PracticeResMsg;
import overcloud.blog.usecase.test.common.PracticeResponse;
import overcloud.blog.usecase.test.get_practices.UserPracticeService;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public RestResponse<List<PracticeResponse>> getUserPractice(String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND));
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

        return resFactory.success(PracticeResMsg.PRACTICE_GET_LIST, practiceResponseList);
    }

}
