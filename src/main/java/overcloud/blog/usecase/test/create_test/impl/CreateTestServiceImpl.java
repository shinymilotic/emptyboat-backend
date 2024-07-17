package overcloud.blog.usecase.test.create_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import overcloud.blog.entity.*;
import overcloud.blog.repository.IQuestionRepository;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.blog.create_article.ArticleRequest;
import overcloud.blog.usecase.common.auth.service.SpringAuthenticationService;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ApiError;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.common.validation.ObjectsValidator;
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
    private final ObjectsValidator<TestRequest> validator;

    public CreateTestServiceImpl(ITestRepository testRepository,
                                 IQuestionRepository questionRepository,
                                 SpringAuthenticationService authenticationService,
                                 ResFactory resFactory,
                                 ObjectsValidator<TestRequest> validator) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.authenticationService = authenticationService;
        this.resFactory = resFactory;
        this.validator = validator;
    }

    @Override
    @Transactional
    public RestResponse<Void> createTest(TestRequest testRequest)  {
        Optional<ApiError> apiError = validator.validate(testRequest);

        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }
        
        List<Question> questions = testRequest.getQuestions();
        LocalDateTime now = LocalDateTime.now();

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();

        TestEntity testEntity = new TestEntity();
        testEntity.setTitle(testRequest.getTitle());
        testEntity.setDescription(testRequest.getDescription());
        testEntity.setAuthorId(currentUser.getId());
        testEntity.setCreatedAt(now);
        testEntity.setUpdatedAt(now);
        TestEntity savedTest = testRepository.save(testEntity);

        // insert questions
        List<QuestionEntity> questionEntities = new ArrayList<>();
        for (Question questionReq : questions) {
            String question = questionReq.getQuestion();
            QuestionEntity questionEntity = new QuestionEntity();
            questionEntity.setQuestion(question);
            questionEntity.setQuestionType(questionReq.getQuestionType());
            if (questionReq.getQuestionType() == 1) {
                ChoiceQuestion choiceQuestion = (ChoiceQuestion) questionReq;
                questionEntity.setAnswers(answerEntities(choiceQuestion.getAnswers(), questionEntity, now));
            }
            questionEntity.setQuestionType(questionReq.getQuestionType());
            questionEntity.setCreatedAt(now);
            questionEntity.setUpdatedAt(now);
            questionEntities.add(questionEntity);
        }
        List<QuestionEntity> savedQuestions = questionRepository.saveAll(questionEntities);

        // insert test_question
        List<TestQuestion> testQuestions = new ArrayList<>();
        int order = 0;
        for (QuestionEntity questionEntity : savedQuestions) {
            TestQuestionId testQuestionId = new TestQuestionId(testEntity.getId(), questionEntity.getId());
            TestQuestion testQuestion = new TestQuestion();
            testQuestion.setId(testQuestionId);
            testQuestion.setTest(testEntity);
            testQuestion.setQuestionOrder(++order);
            testQuestion.setQuestion(questionEntity);
            testQuestions.add(testQuestion);
        }
        savedTest.setQuestions(testQuestions);

        return resFactory.success(TestResMsg.TEST_CREATE_SUCCESS, null);
    }

    public List<AnswerEntity> answerEntities(List<Answer> answers, QuestionEntity question, LocalDateTime now) {
        List<AnswerEntity> answerEntities = new ArrayList<>();

        for (Answer answer : answers) {
            String answerContent = answer.getAnswer();
            boolean truth = answer.isTruth();

            if (!StringUtils.hasText(answerContent)) {
                throw new InvalidDataException(resFactory.fail(TestResMsg.ANSWER_NOT_FOUND));
            }

            AnswerEntity answerEntity = new AnswerEntity();
            answerEntity.setAnswer(answerContent);
            answerEntity.setQuestion(question);
            answerEntity.setTruth(truth);
            answerEntity.setCreatedAt(now);
            answerEntity.setUpdatedAt(now);
            answerEntities.add(answerEntity);
        }

        return answerEntities;
    }
}
