package overcloud.blog.usecase.test.create_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.github.f4b6a3.uuid.UuidCreator;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ApiError;
import overcloud.blog.response.ResFactory;
import overcloud.blog.usecase.test.create_test.request.TestRequest;
import overcloud.blog.usecase.test.create_test.response.Answer;
import overcloud.blog.usecase.test.create_test.response.ChoiceQuestion;
import overcloud.blog.utils.validation.ObjectsValidator;
import overcloud.blog.entity.*;
import overcloud.blog.repository.IChoiceAnswerRepository;
import overcloud.blog.repository.IQuestionRepository;
import overcloud.blog.repository.ITestQuestionRepository;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.test.common.*;
import overcloud.blog.usecase.test.create_test.CreateTestService;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CreateTestServiceImpl implements CreateTestService {
    private final ITestRepository testRepository;
    private final IQuestionRepository questionRepository;
    private final SpringAuthenticationService authenticationService;
    private final ResFactory resFactory;
    private final ITestQuestionRepository testQuestionRepository;
    private final IChoiceAnswerRepository choiceAnswerRepository;
    private final ObjectsValidator validator;

    public CreateTestServiceImpl(ITestRepository testRepository,
                                 IQuestionRepository questionRepository,
                                 SpringAuthenticationService authenticationService,
                                 ResFactory resFactory,
                                 ObjectsValidator validator,
                                 ITestQuestionRepository testQuestionRepository,
                                 IChoiceAnswerRepository choiceAnswerRepository) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.authenticationService = authenticationService;
        this.resFactory = resFactory;
        this.validator = validator;
        this.testQuestionRepository = testQuestionRepository;
        this.choiceAnswerRepository = choiceAnswerRepository;
    }

    public Optional<ApiError> validateQuestions(Optional<ApiError> apiError, List<Question> questions) {
        if (questions == null || questions.isEmpty()) {
            return apiError;
        }

        for (Question question : questions) {
            if (!StringUtils.hasText(question.getQuestion())) {
                apiError = validator.addError(apiError, TestResMsg.TEST_QUESTION_SIZE);
            }

            if (question.getQuestionType() == QuestionType.CHOICE.getValue()) {
                ChoiceQuestion choiceQuestion = (ChoiceQuestion) question;
                apiError = validateAnswer(apiError, choiceQuestion.getAnswers());
            }
        }

        return apiError;
    }

    private Optional<ApiError> validateAnswer(Optional<ApiError> apiError, List<Answer> answers) {
        if (answers == null || answers.isEmpty()) {
            apiError = validator.addError(apiError, TestResMsg.TEST_LIST_ANSWER_SIZE);
            answers = new ArrayList<>();
        }

        for (Answer answer : answers) {
            if (!StringUtils.hasText(answer.getAnswer())) {
                apiError = validator.addError(apiError, TestResMsg.TEST_ANSWER_SIZE);
           }
        }

        return apiError;
    }

    @Override
    @Transactional
    public Void createTest(TestRequest testRequest)  {
        Optional<ApiError> apiError = validator.validate(testRequest);
        apiError = validateQuestions(apiError, testRequest.getQuestions());

        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }
        
        LocalDateTime now = LocalDateTime.now();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();

        TestEntity testEntity = new TestEntity();
        testEntity.setTestId(UuidCreator.getTimeOrderedEpoch());
        testEntity.setTitle(testRequest.getTitle());
        testEntity.setDescription(testRequest.getDescription());
        testEntity.setAuthorId(currentUser.getUserId());
        testEntity.setCreatedAt(now);
        testEntity.setUpdatedAt(now);

        List<QuestionEntity> questionEntities = new ArrayList<>();
        List<ChoiceAnswerEntity> answerEntities = new ArrayList<>();
        for (Question questionReq : testRequest.getQuestions()) {
            String question = questionReq.getQuestion();
            QuestionEntity questionEntity = new QuestionEntity();
            questionEntity.setQuestionId(UuidCreator.getTimeOrderedEpoch());
            questionEntity.setQuestion(question);
            questionEntity.setQuestionType(questionReq.getQuestionType());
            if (questionReq.getQuestionType() == QuestionType.CHOICE.getValue()) {
                ChoiceQuestion choiceQuestion = (ChoiceQuestion) questionReq;
                answerEntities.addAll(answerEntities(choiceQuestion.getAnswers(), questionEntity, now));
            }
            questionEntity.setQuestionType(questionReq.getQuestionType());
            questionEntity.setCreatedAt(now);
            questionEntity.setUpdatedAt(now);
            questionEntities.add(questionEntity);
        }

        List<TestQuestion> testQuestions = new ArrayList<>();
        for (QuestionEntity questionEntity : questionEntities) {
            TestQuestionId testQuestionId = new TestQuestionId(testEntity.getTestId(), questionEntity.getQuestionId());
            TestQuestion testQuestion = new TestQuestion();
            testQuestion.setId(testQuestionId);
            testQuestions.add(testQuestion);
        }
        
        testRepository.save(testEntity);
        questionRepository.saveAll(questionEntities);
        testQuestionRepository.saveAll(testQuestions);
        choiceAnswerRepository.saveAll(answerEntities);

        return null;
    }

    public List<ChoiceAnswerEntity> answerEntities(List<Answer> answers, QuestionEntity question, LocalDateTime now) {
        List<ChoiceAnswerEntity> answerEntities = new ArrayList<>();

        for (Answer answer : answers) {
            String answerContent = answer.getAnswer();
            boolean truth = answer.isTruth();
            ChoiceAnswerEntity entity = new ChoiceAnswerEntity();
            entity.setChoiceAnswerId(UuidCreator.getTimeOrderedEpoch());
            entity.setAnswer(answerContent);
            entity.setQuestionId(question.getQuestionId());
            entity.setTruth(truth);
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            answerEntities.add(entity);
        }

        return answerEntities;
    }
}
