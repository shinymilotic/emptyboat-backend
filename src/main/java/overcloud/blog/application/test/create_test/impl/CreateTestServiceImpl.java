package overcloud.blog.application.test.create_test.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import overcloud.blog.application.article.core.utils.ArticleUtils;
import overcloud.blog.application.test.common.Answer;
import overcloud.blog.application.test.common.ChoiceQuestion;
import overcloud.blog.application.test.common.Question;
import overcloud.blog.application.test.common.TestError;
import overcloud.blog.application.test.common.TestRequest;
import overcloud.blog.application.test.create_test.CreateTestService;
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
    @Transactional
    public String createTest(TestRequest testRequest) {
        List<Question> questions = testRequest.getQuestions();
        LocalDateTime now = LocalDateTime.now();

        List<QuestionEntity> questionEntities = new ArrayList<>();
        for (Question questionReq: questions) {
            String question = questionReq.getQuestion();
            QuestionEntity questionEntity = new QuestionEntity();
            questionEntity.setQuestion(question);
            if (questionReq.getQuestionType() == 1) {
                ChoiceQuestion choiceQuestion = (ChoiceQuestion) questionReq;
                questionEntity.setAnswers(answerEntities(choiceQuestion.getAnswers(), questionEntity, now));
            }
            questionEntity.setCreatedAt(now);
            questionEntity.setUpdatedAt(now);
            questionEntities.add(questionEntity);
        }

        TestEntity testEntity = new TestEntity();
        testEntity.setTitle(testRequest.getTitle());
        testEntity.setQuestions(questionEntities);
        testEntity.setSlug(ArticleUtils.toSlug(testRequest.getTitle()));
        testEntity.setCreatedAt(now);
        testEntity.setUpdatedAt(now);
        testRepository.save(testEntity);

        return testRequest.getTitle();
    }

    public List<AnswerEntity> answerEntities(List<Answer> answers, QuestionEntity question, LocalDateTime now) {
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
            answerEntity.setQuestion(question);
            answerEntity.setTruth(truth);
            answerEntity.setCreatedAt(now);
            answerEntity.setUpdatedAt(now);
            answerEntities.add(answerEntity);
        }

        return answerEntities;
    }
}
