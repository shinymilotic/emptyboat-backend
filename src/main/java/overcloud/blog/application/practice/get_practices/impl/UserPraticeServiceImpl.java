package overcloud.blog.application.practice.get_practices.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import overcloud.blog.application.practice.core.PracticeResponse;
import overcloud.blog.application.practice.core.UserPracticeResponse;
import overcloud.blog.application.practice.get_practices.UserPracticeService;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.entity.PracticeEntity;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.PracticeRepository;
import overcloud.blog.repository.TestRepository;

@Service
public class UserPraticeServiceImpl implements UserPracticeService{

    private final PracticeRepository practiceRepository;

    private final UserRepository userRepository;

    private final TestRepository testRepository;

    UserPraticeServiceImpl(PracticeRepository practiceRepository,
        UserRepository userRepository,
        TestRepository testRepository) {
        this.practiceRepository = practiceRepository;
        this.userRepository = userRepository;
        this.testRepository = testRepository;
    }

    @Override
    public UserPracticeResponse getUserPractice(String username) {
        // UserEntity user = userRepository.findByUsername(username);

        // if (user == null) {
        //     // throw
        // }

        // List<PracticeEntity> practices = practiceRepository.findByTesterId(user.getId());
        // List<UUID> pIdList = new ArrayList<UUID>();
        // for (PracticeEntity practiceEntity : practices) {
        //     pIdList.add(practiceEntity.getId());
        // }
        // List<TestEntity> tests = practiceRepository.findAllById(pIdList);
        // tests,

        // UserPracticeResponse response = new UserPracticeResponse();

        // List<PracticeResponse> practiceResponseList = new ArrayList<>();
        // for (PracticeEntity practiceEntity : practices) {
        //     TestEntity test = testMap.get(practiceEntity.getId());
        //     LocalDateTime createdAt = testMap.get(practiceEntity.getId()).getCreatedAt();
        //     PracticeResponse practice = PracticeResponse.builder()
        //         .testTitle(test.getTitle())
        //         .date(createdAt)
        //         .score(practiceEntity.getScore()).build();
        //     practiceResponseList.add(practice);
        // }
        // response.setPractices(practiceResponseList);

        // return response;
        return null;
    }
    
}
