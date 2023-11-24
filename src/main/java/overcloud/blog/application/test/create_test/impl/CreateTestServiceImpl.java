package overcloud.blog.application.test.create_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import overcloud.blog.application.test.*;
import overcloud.blog.application.test.create_test.CreateTestService;
import overcloud.blog.application.user.core.UserError;
import overcloud.blog.entity.AnswerEntity;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.repository.TestRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CreateTestServiceImpl implements CreateTestService {

    private final TestRepository testRepository;

    public CreateTestServiceImpl(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public String createTest(TestRequest testRequest) {
        List<Question> questions = testRequest.getQuestions();
        LocalDateTime now = LocalDateTime.now();

        Set<QuestionEntity> questionEntities = new HashSet<>();
        for (Question questionReq: questions) {
            String question = questionReq.getQuestion();
            QuestionEntity questionEntity = new QuestionEntity();
            questionEntity.setQuestion(question);
            questionEntity.setAnswers(answerEntities(questionReq.getAnswers(), now));
            questionEntity.setCreatedAt(now);
            questionEntity.setUpdatedAt(now);
            questionEntities.add(questionEntity);
        }

        TestEntity testEntity = new TestEntity();
        testEntity.setTitle(testRequest.getTitle());
        testEntity.setQuestions(questionEntities);
        testRepository.save(testEntity);

        return testRequest.getTitle();
    }

    public List<AnswerEntity> answerEntities(List<Answer> answers, LocalDateTime now) {
        List<AnswerEntity> answerEntities = new ArrayList<>();

        for (Answer answer: answers) {
            String answerContent = answer.getAnswer();
            boolean truth = answer.isTruth();

            if(!StringUtils.hasText(answerContent)) {
                // error
                throw new InvalidDataException(ApiError.from(TestError.ANSWER_EMPTY));
            }

            AnswerEntity answerEntity = new AnswerEntity();
            answerEntity.setAnswer(answerContent);
            answerEntity.setTruth(truth);
            answerEntity.setCreatedAt(now);
            answerEntity.setUpdatedAt(now);
            answerEntities.add(answerEntity);
        }

        return answerEntities;
    }
}
