package overcloud.blog.usecase.test.create_practice.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.f4b6a3.uuid.UuidCreator;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.*;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ResFactory;
import overcloud.blog.repository.IPracticeOpenAnswerRepository;
import overcloud.blog.repository.IPracticeChoiceRepository;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.test.common.PracticeResMsg;
import overcloud.blog.usecase.test.common.QuestionType;
import overcloud.blog.usecase.test.create_practice.request.IQuestionPractice;
import overcloud.blog.usecase.test.create_practice.request.PracticeChoiceQuestion;
import overcloud.blog.usecase.test.create_practice.request.PracticeOpenQuestion;
import overcloud.blog.usecase.test.create_practice.request.PracticeRequest;
import overcloud.blog.usecase.test.create_practice.response.CreatePracticeResponse;
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
    public CreatePracticeResponse createPractice(PracticeRequest practiceRequest) {
        List<IQuestionPractice> practices = practiceRequest.getPractices();
        UUID testId = UUID.fromString(practiceRequest.getTestId());
        LocalDateTime now = LocalDateTime.now();

        if (practices == null || practices.isEmpty()) {
            throw new InvalidDataException(resFactory.fail(PracticeResMsg.PRACTICE_CREATE_FAILED));
        }

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();

        Optional<TestEntity> testEntity = testRepository.findById(testId);

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
        List<PracticeOpenAnswerEntity> openAnswerEntities = new ArrayList<>();

        for (IQuestionPractice practice : practices) {

            UUID questionId = UUID.fromString(practice.getQuestionId());
            Integer questionType = practice.getQuestionType();

            if (questionType.equals(QuestionType.CHOICE.getValue())) {
                PracticeChoiceQuestion practiceChoiceQuestion = (PracticeChoiceQuestion) practice;

                for (String answerId : practiceChoiceQuestion.getAnswer()) {
                    PracticeChoiceAnswerId practiceChoiceAnswerId = new PracticeChoiceAnswerId();
                    practiceChoiceAnswerId.setPracticeId(practiceEntity.getPracticeId());
                    practiceChoiceAnswerId.setChoiceAnswerId(UUID.fromString(answerId));
                    PracticeChoiceAnswerEntity entity = new PracticeChoiceAnswerEntity();
                    entity.setId(practiceChoiceAnswerId);
                    choiceEntities.add(entity);
                }
            } else if (questionType.equals(QuestionType.OPEN.getValue())) {
                openAnswerEntities.add(getPracticeOpenAnswerEntity(practiceEntity, (PracticeOpenQuestion) practice, questionId, now));
            }
        }

        practiceChoiceRepository.saveAll(choiceEntities);
        openAnswerRepository.saveAll(openAnswerEntities);

        CreatePracticeResponse response = new CreatePracticeResponse();
        response.setPracticeId(practiceEntity.getPracticeId());

        return response;
    }

    private static PracticeOpenAnswerEntity getPracticeOpenAnswerEntity(PracticeEntity practiceEntity, PracticeOpenQuestion practice, UUID questionId, LocalDateTime now) {
        PracticeOpenQuestion practiceOpenQuestion = practice;
        PracticeOpenAnswerId practiceOpenAnswerId = new PracticeOpenAnswerId();
        practiceOpenAnswerId.setPracticeId(practiceEntity.getPracticeId());
        practiceOpenAnswerId.setQuestionId(questionId);

        PracticeOpenAnswerEntity entity = new PracticeOpenAnswerEntity();
        entity.setId(practiceOpenAnswerId);
        entity.setAnswer(practiceOpenQuestion.getAnswer());
        entity.setCreatedAt(now);
        return entity;
    }
}
