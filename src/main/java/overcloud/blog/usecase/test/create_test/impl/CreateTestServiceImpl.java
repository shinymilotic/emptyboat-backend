package overcloud.blog.usecase.test.create_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import overcloud.blog.entity.AnswerEntity;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;
import overcloud.blog.repository.jparepository.JpaTestRepository;
import overcloud.blog.usecase.blog.common.ArticleUtils;
import overcloud.blog.usecase.test.common.*;
import overcloud.blog.usecase.test.create_test.CreateTestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreateTestServiceImpl implements CreateTestService {

    private final JpaTestRepository testRepository;

    public CreateTestServiceImpl(JpaTestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    @Transactional
    public void createTest(TestRequest testRequest) {
        List<Question> questions = testRequest.getQuestions();
        LocalDateTime now = LocalDateTime.now();

        List<QuestionEntity> questionEntities = new ArrayList<>();
        for (Question questionReq : questions) {
            String question = questionReq.getQuestion();
            QuestionEntity questionEntity = new QuestionEntity();
            questionEntity.setQuestion(question);
            if (questionReq.getQuestionType() == 1) {
                ChoiceQuestion choiceQuestion = (ChoiceQuestion) questionReq;
                questionEntity.setAnswers(answerEntities(choiceQuestion.getAnswers(), questionEntity, now));
            }
            questionEntity.setQuestionType(questionReq.getQuestionType());
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
    }

    public List<AnswerEntity> answerEntities(List<Answer> answers, QuestionEntity question, LocalDateTime now) {
        List<AnswerEntity> answerEntities = new ArrayList<>();

        for (Answer answer : answers) {
            String answerContent = answer.getAnswer();
            boolean truth = answer.isTruth();

            if (!StringUtils.hasText(answerContent)) {
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
