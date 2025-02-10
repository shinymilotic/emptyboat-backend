package overcloud.blog.usecase.test.update_test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.f4b6a3.uuid.UuidCreator;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ApiError;
import overcloud.blog.response.ResFactory;
import overcloud.blog.utils.validation.ObjectsValidator;
import overcloud.blog.entity.ChoiceAnswerEntity;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.repository.IChoiceAnswerRepository;
import overcloud.blog.repository.IPracticeChoiceRepository;
import overcloud.blog.repository.IPracticeOpenAnswerRepository;
import overcloud.blog.repository.IQuestionRepository;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.test.common.QuestionType;
import overcloud.blog.usecase.user.common.UpdateFlg;

@Service
public class UpdateTestServiceImpl implements UpdateTestService {
    private final ObjectsValidator<TestUpdateRequest> validator;
    private final ResFactory resFactory;
    private final ITestRepository testRepo;
    private final IQuestionRepository questionRepo;
    private final IChoiceAnswerRepository choiceAnswerRepo;
    private final IPracticeOpenAnswerRepository practiceOpenAnswerRepo;
    private final IPracticeChoiceRepository practiceChoiceRepo;

    public UpdateTestServiceImpl(ObjectsValidator<TestUpdateRequest> validator,
            ResFactory resFactory,
            ITestRepository testRepo,
            IQuestionRepository questionRepo,
            IChoiceAnswerRepository choiceAnswerRepo,
            IPracticeOpenAnswerRepository practiceOpenAnswerRepo,
            IPracticeChoiceRepository practiceChoiceRepo) {
        this.validator = validator;
        this.resFactory = resFactory;
        this.testRepo = testRepo;
        this.questionRepo = questionRepo;
        this.choiceAnswerRepo = choiceAnswerRepo;
        this.practiceOpenAnswerRepo = practiceOpenAnswerRepo;
        this.practiceChoiceRepo = practiceChoiceRepo;
    }

    @Override
    @Transactional
    public Void updateTest(String id, TestUpdateRequest request) {
        UUID testId = UUID.fromString(id);
        Optional<ApiError> apiError = validator.validate(request);
        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }
        List<UpdQuestion> questions = request.getQuestions();
        List<QuestionEntity> questionsToInsert = new ArrayList<>();
        List<QuestionEntity> questionsToUpdate = new ArrayList<>();
        List<UUID> questionsToDelete = new ArrayList<>();
        List<ChoiceAnswerEntity> answersToInsert = new ArrayList<>();
        List<ChoiceAnswerEntity> answersToUpdate = new ArrayList<>();
        List<UUID> answersToDelete = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (UpdQuestion question : questions) {
            if (question.getUpdateFlg().equals(UpdateFlg.NEW.getValue())) {
                UUID questionId = UuidCreator.getTimeOrderedEpoch();
                questionsToInsert.add(QuestionEntity.builder()
                        .questionId(questionId)
                        .question(question.getQuestion())
                        .questionType(question.getQuestionType())
                        .testId(testId)
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
                );

                if (question.getQuestionType().equals(QuestionType.CHOICE.getValue())) {
                    ((UpdChoiceQuestion) question).getAnswers()
                        .forEach(answer -> answersToInsert.add(ChoiceAnswerEntity.builder()
                            .choiceAnswerId(UuidCreator.getTimeOrderedEpoch())
                            .answer(answer.getAnswer())
                            .truth(answer.getTruth())
                            .questionId(questionId)
                            .createdAt(now)
                            .updatedAt(now).build()));
                }
            } else if (question.getUpdateFlg().equals(UpdateFlg.UPDATE.getValue())) {
                UUID questionId = UUID.fromString(question.getId());
                questionsToUpdate.add(QuestionEntity.builder()
                        .questionId(questionId)
                        .question(question.getQuestion())
                        .questionType(question.getQuestionType())
                        .testId(testId)
                        .updatedAt(now).build()
                );

                if (question.getQuestionType().equals(QuestionType.CHOICE.getValue())) {
                    ((UpdChoiceQuestion) question).getAnswers()
                        .forEach(answer -> 
                            answersToUpdate.add(ChoiceAnswerEntity.builder()
                            .choiceAnswerId(answer.getUpdateFlg().equals(UpdateFlg.NEW.getValue()) ?
                                UuidCreator.getTimeOrderedEpoch() :
                                UUID.fromString(answer.getId()))
                            .answer(answer.getAnswer())
                            .truth(answer.getTruth())
                            .questionId(questionId)
                            .createdAt(now)
                            .updatedAt(now).build()
                        ));
                }
            } else if (question.getUpdateFlg().equals(UpdateFlg.DELETE.getValue())) {
                questionsToDelete.add(UUID.fromString(question.getId()));

                if (question.getQuestionType().equals(QuestionType.CHOICE.getValue())) {
                    ((UpdChoiceQuestion) question).getAnswers()
                        .forEach(answer -> answersToDelete.add(UUID.fromString(answer.getId())));
                }
            }
        }

        testRepo.updateTest(testId, request.getTitle(), request.getDescription());

        if (!questionsToDelete.isEmpty()) {
            practiceOpenAnswerRepo.deleteAllByQuestionId(questionsToDelete);
            questionRepo.deleteAll(questionsToDelete);
        }

        if (!answersToDelete.isEmpty()) {
            choiceAnswerRepo.deleteAll(answersToDelete);
            practiceChoiceRepo.deleteAll(answersToDelete);
        }

        if (!questionsToInsert.isEmpty()) {
            questionRepo.saveAll(questionsToInsert);
        }

        if (!answersToInsert.isEmpty()) {
            choiceAnswerRepo.saveAll(answersToInsert);
        }

        if (!questionsToUpdate.isEmpty()) {
            questionRepo.updateAll(questionsToUpdate);
        }

        if (!answersToUpdate.isEmpty()) {
            choiceAnswerRepo.updateAll(answersToUpdate);    
        }
        
        return null;
    }
}
