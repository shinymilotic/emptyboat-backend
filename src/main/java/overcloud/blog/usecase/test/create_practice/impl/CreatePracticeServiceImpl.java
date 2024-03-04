package overcloud.blog.usecase.test.create_practice.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.*;
import overcloud.blog.infrastructure.auth.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.repository.jparepository.JpaEssayAnswerRepository;
import overcloud.blog.repository.jparepository.JpaPracticeChoiceRepository;
import overcloud.blog.repository.jparepository.JpaTestRepository;
import overcloud.blog.usecase.auth.common.UserError;
import overcloud.blog.usecase.test.common.ChoiceAnswer;
import overcloud.blog.usecase.test.common.EssayAnswer;
import overcloud.blog.usecase.test.common.PracticeRequest;
import overcloud.blog.usecase.test.create_practice.CreatePracticeResponse;
import overcloud.blog.usecase.test.create_practice.CreatePracticeService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CreatePracticeServiceImpl implements CreatePracticeService {

    private final IPracticeRepository practiceRepository;
    private final JpaTestRepository testRepository;
    private final SpringAuthenticationService authenticationService;
    private final JpaPracticeChoiceRepository practiceChoiceRepository;
    private final JpaEssayAnswerRepository essayAnswerRepository;

    CreatePracticeServiceImpl(IPracticeRepository practiceRepository,
                              SpringAuthenticationService authenticationService,
                              JpaTestRepository testRepository,
                              JpaPracticeChoiceRepository practiceChoiceRepository,
                              JpaEssayAnswerRepository essayAnswerRepository) {
        this.practiceRepository = practiceRepository;
        this.authenticationService = authenticationService;
        this.testRepository = testRepository;
        this.practiceChoiceRepository = practiceChoiceRepository;
        this.essayAnswerRepository = essayAnswerRepository;
    }

    @Override
    @Transactional
    public CreatePracticeResponse createPractice(PracticeRequest practiceRequest) {
        String slug = practiceRequest.getSlug();
        List<ChoiceAnswer> choices = practiceRequest.getChoiceAnswers();
        List<EssayAnswer> essayAnswers = practiceRequest.getEssayAnswers();
        LocalDateTime now = LocalDateTime.now();

        if (choices == null || choices.isEmpty()) {
            // throw new InvalidDataException(null);
        }

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(UserError.USER_NOT_FOUND))
                .getUser();

        Optional<TestEntity> testEntity = testRepository.findBySlug(slug);

        if (!testEntity.isPresent()) {
            //throw exception
        }

        PracticeEntity practiceEntity = new PracticeEntity();
        practiceEntity.setTestId(testEntity.get().getId());
        practiceEntity.setTesterId(currentUser.getId());
        practiceEntity.setCreatedAt(now);
        practiceEntity = practiceRepository.save(practiceEntity);

        List<PracticeChoiceEntity> choiceEntities = new ArrayList<>();
        for (ChoiceAnswer choice : choices) {
            for (String answerId : choice.getAnswer()) {
                PracticeChoiceEntity choiceEntity = new PracticeChoiceEntity();
                choiceEntity.setAnswerId(UUID.fromString(answerId));
                choiceEntity.setPracticeId(practiceEntity.getId());
                choiceEntities.add(choiceEntity);
            }
        }

        List<EssayAnswerEntity> essayAnswerEntities = new ArrayList<>();
        for (EssayAnswer answer : essayAnswers) {
            EssayAnswerEntity essayAnswerEntity = new EssayAnswerEntity();
            essayAnswerEntity.setPracticeId(practiceEntity.getId());
            essayAnswerEntity.setQuestionId(UUID.fromString(answer.getQuestionId()));
            essayAnswerEntity.setAnswer(answer.getAnswer());
            essayAnswerEntity.setCreatedAt(now);
            essayAnswerEntities.add(essayAnswerEntity);
        }
        practiceChoiceRepository.saveAll(choiceEntities);
        essayAnswerRepository.saveAll(essayAnswerEntities);

        CreatePracticeResponse response = new CreatePracticeResponse();
        response.setPracticeId(practiceEntity.getId());

        return response;
    }
}
