package overcloud.blog.application.test.get_test.impl;

import org.springframework.stereotype.Service;
import overcloud.blog.application.test.common.Answer;
import overcloud.blog.application.test.common.Question;
import overcloud.blog.application.test.common.TestResponse;
import overcloud.blog.application.test.get_test.GetTestService;
import overcloud.blog.entity.AnswerEntity;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.repository.TestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GetTestServiceImpl implements GetTestService {

    private final TestRepository testRepository;

    public GetTestServiceImpl(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public TestResponse getTest(String slug) {
        Optional<TestEntity> testEntity = this.testRepository.findBySlug(slug);

        if(!testEntity.isPresent()) {
            // do something...
        }

        String titleDB = testEntity.get().getTitle();
        String slugDB = testEntity.get().getSlug();

        return TestResponse.testResponseFactory(
                titleDB,
                slugDB,
                getQuestions(testEntity.get().getQuestions()));
    }

    public List<Question> getQuestions(List<QuestionEntity> questionEntities) {
        List<Question> questions = new ArrayList<>();

        for (QuestionEntity questionEntity: questionEntities) {
            Question question = Question.questionFactory(questionEntity.getQuestion(), getAnswers(questionEntity));
            questions.add(question);
        }

        return questions;
    }


    public List<Answer> getAnswers(QuestionEntity questionEntity) {
        List<AnswerEntity> answerEntities = questionEntity.getAnswers();
        List<Answer> answers = new ArrayList<>();
        for (AnswerEntity answerEntity: answerEntities) {
            String answerString = answerEntity.getAnswer();
            boolean truth = answerEntity.isTruth();
            Answer answer = Answer.builder().answer(answerString).truth(truth).build();
            answers.add(answer);
        }

        return answers;
    }

}
