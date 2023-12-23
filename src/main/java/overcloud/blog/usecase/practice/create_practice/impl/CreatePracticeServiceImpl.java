package overcloud.blog.usecase.practice.create_practice.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import overcloud.blog.entity.EssayAnswerEntity;
import overcloud.blog.entity.PracticeChoiceEntity;
import overcloud.blog.entity.PracticeEntity;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.repository.jparepository.JpaEssayAnswerRepository;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.repository.jparepository.JpaPracticeChoiceRepository;
import overcloud.blog.repository.jparepository.JpaTestRepository;
import overcloud.blog.usecase.practice.core.EssayAnswer;
import overcloud.blog.usecase.practice.core.PracticeRequest;
import overcloud.blog.usecase.practice.create_practice.CreatePracticeService;
import overcloud.blog.usecase.user.core.UserError;

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
    public void createPractice(PracticeRequest practiceRequest) {
        String slug = practiceRequest.getSlug();
        List<String> choices = practiceRequest.getChoiceAnswers();
        List<EssayAnswer> essayAnswers = practiceRequest.getEssayAnswers();
        LocalDateTime now = LocalDateTime.now();

        if(choices == null || choices.isEmpty()) {
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
        for (String choice : choices) {
            PracticeChoiceEntity choiceEntity = new PracticeChoiceEntity();
            choiceEntity.setAnswerId(UUID.fromString(choice));
            choiceEntity.setPracticeId(practiceEntity.getId());
            choiceEntities.add(choiceEntity);
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
        practiceChoiceRepository.saveAll((Iterable<PracticeChoiceEntity>)choiceEntities);
        essayAnswerRepository.saveAll((Iterable<EssayAnswerEntity>) essayAnswerEntities);
    }
}
