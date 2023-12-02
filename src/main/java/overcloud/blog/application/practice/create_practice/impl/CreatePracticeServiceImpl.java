package overcloud.blog.application.practice.create_practice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.kafka.common.Uuid;
import org.springframework.stereotype.Service;

import overcloud.blog.application.practice.core.PracticeRequest;
import overcloud.blog.application.practice.create_practice.CreatePracticeService;
import overcloud.blog.application.user.core.UserError;
import overcloud.blog.entity.PracticeChoiceEntity;
import overcloud.blog.entity.PracticeEntity;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.repository.PracticeChoiceRepository;
import overcloud.blog.repository.PracticeRepository;
import overcloud.blog.repository.TestRepository;

@Service
public class CreatePracticeServiceImpl implements CreatePracticeService {

    private final PracticeRepository practiceRepository;
    private final TestRepository testRepository;
    private final SpringAuthenticationService authenticationService;
    private final PracticeChoiceRepository practiceChoiceRepository;

    CreatePracticeServiceImpl(PracticeRepository practiceRepository, 
            SpringAuthenticationService authenticationService,
            TestRepository testRepository,
            PracticeChoiceRepository practiceChoiceRepository) {
        this.practiceRepository = practiceRepository;
        this.authenticationService = authenticationService;
        this.testRepository = testRepository;
        this.practiceChoiceRepository = practiceChoiceRepository;
    }

    @Override
    public boolean createPractice(PracticeRequest practiceRequest) {
        List<String> answerIds = practiceRequest.getAnswerIds();
        UUID testId = UUID.fromString(practiceRequest.getTestId());
        List<String> choices = practiceRequest.getAnswerIds();
        if(answerIds == null || answerIds.isEmpty()) {
            throw new InvalidDataException(null);
        }

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();
                
        Optional<TestEntity> testEntity = testRepository.findById(testId);
        if (!testEntity.isPresent()) {
            //throw exception
        }
        PracticeEntity practiceEntity = new PracticeEntity();
        practiceEntity.setTest(testEntity.get());
        practiceEntity.setTester(currentUser);

        List<PracticeChoiceEntity> choiceEntities = new ArrayList<>();
        for (String choice : choices) {
            PracticeChoiceEntity choiceEntity = new PracticeChoiceEntity();
            choiceEntity.setAnswerId(UUID.fromString(choice));
            choiceEntity.setPractice(practiceEntity);
            choiceEntities.add(choiceEntity);
        }

        practiceChoiceRepository.saveAll((Iterable)choiceEntities);
        practiceRepository.save(practiceEntity);

        return true;
    }
    
}
