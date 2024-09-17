package overcloud.blog.usecase.test.create_practice.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.f4b6a3.uuid.UuidCreator;

import overcloud.blog.common.auth.service.SpringAuthenticationService;
import overcloud.blog.common.exceptionhandling.InvalidDataException;
import overcloud.blog.common.response.ResFactory;
import overcloud.blog.common.response.RestResponse;
import overcloud.blog.entity.*;
import overcloud.blog.repository.IEssayAnswerRepository;
import overcloud.blog.repository.IPracticeChoiceRepository;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.test.common.ChoiceAnswer;
import overcloud.blog.usecase.test.common.EssayAnswer;
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
    private final IEssayAnswerRepository essayAnswerRepository;
    private final ResFactory resFactory;

    CreatePracticeServiceImpl(IPracticeRepository practiceRepository,
                              SpringAuthenticationService authenticationService,
                              ITestRepository testRepository,
                              IPracticeChoiceRepository practiceChoiceRepository,
                              IEssayAnswerRepository essayAnswerRepository,
                              ResFactory resFactory) {
        this.practiceRepository = practiceRepository;
        this.authenticationService = authenticationService;
        this.testRepository = testRepository;
        this.practiceChoiceRepository = practiceChoiceRepository;
        this.essayAnswerRepository = essayAnswerRepository;
        this.resFactory = resFactory;
    }

    @Override
    @Transactional
    public RestResponse<CreatePracticeResponse> createPractice(PracticeRequest practiceRequest) {
        UUID id = UUID.fromString(practiceRequest.getId());
        List<ChoiceAnswer> choices = practiceRequest.getChoiceAnswers();
        List<EssayAnswer> essayAnswers = practiceRequest.getEssayAnswers();
        LocalDateTime now = LocalDateTime.now();

        if ((choices == null || choices.isEmpty()) && 
            (essayAnswers == null || essayAnswers.isEmpty())) {
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

        List<PracticeChoiceEntity> choiceEntities = new ArrayList<>();
        for (ChoiceAnswer choice : choices) {
            for (String answerId : choice.getAnswer()) {
                PracticeChoiceEntity choiceEntity = new PracticeChoiceEntity();
                choiceEntity.setPracticeChoiceId(UuidCreator.getTimeOrderedEpoch());
                choiceEntity.setAnswerId(UUID.fromString(answerId));
                choiceEntity.setPracticeId(practiceEntity.getPracticeId());
                choiceEntities.add(choiceEntity);
            }
        }

        List<EssayAnswerEntity> essayAnswerEntities = new ArrayList<>();
        for (EssayAnswer answer : essayAnswers) {
            EssayAnswerEntity essayAnswerEntity = new EssayAnswerEntity();
            essayAnswerEntity.setEssayAnswerId(UuidCreator.getTimeOrderedEpoch());
            essayAnswerEntity.setPracticeId(practiceEntity.getPracticeId());
            essayAnswerEntity.setQuestionId(UUID.fromString(answer.getQuestionId()));
            essayAnswerEntity.setAnswer(answer.getAnswer());
            essayAnswerEntity.setCreatedAt(now);
            essayAnswerEntities.add(essayAnswerEntity);
        }
        practiceChoiceRepository.saveAll(choiceEntities);
        essayAnswerRepository.saveAll(essayAnswerEntities);

        CreatePracticeResponse response = new CreatePracticeResponse();
        response.setPracticeId(practiceEntity.getPracticeId());

        return resFactory.success(PracticeResMsg.PRACTICE_CREATE_SUCCESS, response);
    }
}
