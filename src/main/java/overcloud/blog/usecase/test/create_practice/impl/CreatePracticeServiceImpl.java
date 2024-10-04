package overcloud.blog.usecase.test.create_practice.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.f4b6a3.uuid.UuidCreator;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
import overcloud.blog.entity.*;
import overcloud.blog.repository.IPracticeOpenAnswerRepository;
import overcloud.blog.repository.IPracticeChoiceRepository;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.test.common.ChoiceAnswer;
import overcloud.blog.usecase.test.common.OpenAnswer;
import overcloud.blog.usecase.test.common.PracticeRequest;
import overcloud.blog.usecase.test.common.PracticeResMsg;
import overcloud.blog.usecase.test.create_practice.CreatePracticeResponse;
import overcloud.blog.usecase.test.create_practice.CreatePracticeService;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CreatePracticeServiceImpl implements CreatePracticeService {
    private final IPracticeRepository practiceRepository;
    private final ITestRepository testRepository;
    private final SpringAuthenticationService authenticationService;
    private final IPracticeChoiceRepository practiceChoiceRepository;
    private final IPracticeOpenAnswerRepository openAnswerRepository;
    private final ResFactory resFactory;

    CreatePracticeServiceImpl(IPracticeRepository practiceRepository,
                              SpringAuthenticationService authenticationService,
                              ITestRepository testRepository,
                              IPracticeChoiceRepository practiceChoiceRepository,
                              IPracticeOpenAnswerRepository openAnswerRepository,
                              ResFactory resFactory) {
        this.practiceRepository = practiceRepository;
        this.authenticationService = authenticationService;
        this.testRepository = testRepository;
        this.practiceChoiceRepository = practiceChoiceRepository;
        this.openAnswerRepository = openAnswerRepository;
        this.resFactory = resFactory;
    }

    @Override
    @Transactional
    public RestResponse<CreatePracticeResponse> createPractice(PracticeRequest practiceRequest) {
        UUID id = UUID.fromString(practiceRequest.getId());
        List<ChoiceAnswer> choices = practiceRequest.getChoiceAnswers();
        List<OpenAnswer> openAnswers = practiceRequest.getOpenAnswers();
        LocalDateTime now = LocalDateTime.now();

        if ((choices == null || choices.isEmpty()) && 
            (openAnswers == null || openAnswers.isEmpty())) {
            throw new InvalidDataException(resFactory.fail(PracticeResMsg.PRACTICE_CREATE_FAILED));
        }

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();

        Optional<TestEntity> testEntity = testRepository.findById(id);

        if (!testEntity.isPresent()) {
            throw new InvalidDataException(resFactory.fail(PracticeResMsg.PRACTICE_CREATE_FAILED));
        }

        PracticeEntity practiceEntity = new PracticeEntity();
        practiceEntity.setPracticeId(UuidCreator.getTimeOrderedEpoch());
        practiceEntity.setTestId(testEntity.get().getTestId());
        practiceEntity.setTesterId(currentUser.getUserId());
        practiceEntity.setCreatedAt(now);
        practiceEntity = practiceRepository.save(practiceEntity);

        List<PracticeChoiceAnswerEntity> choiceEntities = new ArrayList<>();
        for (ChoiceAnswer choice : choices) {
            for (String answerId : choice.getAnswer()) {
                PracticeChoiceAnswerEntity choiceEntity = new PracticeChoiceAnswerEntity();
                PracticeChoiceAnswerId practiceChoiceQuestionId = new PracticeChoiceAnswerId();
                practiceChoiceQuestionId.setChoiceAnswerId(UUID.fromString(answerId));
                practiceChoiceQuestionId.setPracticeId(practiceEntity.getPracticeId());
                choiceEntity.setPracticeChoiceAnswerId(practiceChoiceQuestionId);
                choiceEntities.add(choiceEntity);
            }
        }

        List<PracticeOpenAnswerEntity> openAnswerEntities = new ArrayList<>();
        for (OpenAnswer answer : openAnswers) {
            PracticeOpenAnswerEntity openAnswerEntity = new PracticeOpenAnswerEntity();
            PracticeOpenAnswerId openAnswerId = new PracticeOpenAnswerId();
            openAnswerId.setPracticeId(practiceEntity.getPracticeId());
            openAnswerId.setQuestionId(UUID.fromString(answer.getQuestionId()));
            openAnswerEntity.setId(openAnswerId);
            openAnswerEntity.setAnswer(answer.getAnswer());
            openAnswerEntity.setCreatedAt(now);
            openAnswerEntities.add(openAnswerEntity);
        }
        practiceChoiceRepository.saveAll(choiceEntities);
        openAnswerRepository.saveAll(openAnswerEntities);

        CreatePracticeResponse response = new CreatePracticeResponse();
        response.setPracticeId(practiceEntity.getPracticeId());

        return resFactory.success(PracticeResMsg.PRACTICE_CREATE_SUCCESS, response);
    }
}
