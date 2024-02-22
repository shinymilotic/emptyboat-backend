package overcloud.blog.usecase.test.get_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.AnswerEntity;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.entity.TestQuestion;
import overcloud.blog.repository.jparepository.JpaTestRepository;
import overcloud.blog.usecase.test.common.Answer;
import overcloud.blog.usecase.test.common.EssayQuestion;
import overcloud.blog.usecase.test.common.Question;
import overcloud.blog.usecase.test.common.TestResponse;
import overcloud.blog.usecase.test.get_test.ChoiceQuestion;
import overcloud.blog.usecase.test.get_test.GetTestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetTestServiceImpl implements GetTestService {

    private final JpaTestRepository testRepository;

    public GetTestServiceImpl(JpaTestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    @Transactional
    public TestResponse getTest(String slug) {
        Optional<TestEntity> testEntity = this.testRepository.findBySlug(slug);

        if (!testEntity.isPresent()) {
            // do something...
        }

        String titleDB = testEntity.get().getTitle();
        String slugDB = testEntity.get().getSlug();
        String descriptionDB = testEntity.get().getDescription();

        return TestResponse.testResponseFactory(
                titleDB,
                descriptionDB,
                slugDB,
                getQuestions(testEntity.get().getQuestions()));
    }

    public List<Question> getQuestions(List<TestQuestion> testQuestions) {
        List<Question> questions = new ArrayList<>();

        for (TestQuestion testQuestion : testQuestions) {
            QuestionEntity questionEntity = testQuestion.getQuestion();
            if (questionEntity.getQuestionType() == 1) {
                List<AnswerEntity> answers = questionEntity.getAnswers();
                long countRightAnswers = answers.stream().filter(e -> e.isTruth()).count();
                if (countRightAnswers > 1) {
                    questions.add(ChoiceQuestion.questionFactory(
                            questionEntity.getId().toString(),
                            questionEntity.getQuestion(),
                            getAnswers(questionEntity),
                            true));
                } else if (countRightAnswers == 1) {
                    questions.add(ChoiceQuestion.questionFactory(
                            questionEntity.getId().toString(),
                            questionEntity.getQuestion(),
                            getAnswers(questionEntity),
                            false));
                } else {
                    // throw err...
                }
//                questions.add(question);
            } else if (questionEntity.getQuestionType() == 2) {
                EssayQuestion question = EssayQuestion.questionFactory(
                        questionEntity.getId().toString(),
                        questionEntity.getQuestion());
                questions.add(question);
            }
        }

        return questions;
    }


    public List<Answer> getAnswers(QuestionEntity questionEntity) {
        List<AnswerEntity> answerEntities = questionEntity.getAnswers();
        List<Answer> answers = new ArrayList<>();
        for (AnswerEntity answerEntity : answerEntities) {
            UUID id = answerEntity.getId();
            String answerString = answerEntity.getAnswer();
            boolean truth = answerEntity.isTruth();
            Answer answer = Answer.answerFactory(id, answerString, truth);
            answers.add(answer);
        }

        return answers;
    }

}
