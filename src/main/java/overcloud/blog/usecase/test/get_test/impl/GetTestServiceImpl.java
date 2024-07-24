package overcloud.blog.usecase.test.get_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.AnswerEntity;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.entity.TestQuestion;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.test.common.Answer;
import overcloud.blog.usecase.test.common.EssayQuestion;
import overcloud.blog.usecase.test.common.Question;
import overcloud.blog.usecase.test.common.TestResMsg;
import overcloud.blog.usecase.test.common.TestResponse;
import overcloud.blog.usecase.test.get_test.ChoiceQuestion;
import overcloud.blog.usecase.test.get_test.GetTestService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetTestServiceImpl implements GetTestService {
    private final ITestRepository testRepository;
    private final ResFactory resFactory;

    public GetTestServiceImpl(ITestRepository testRepository, ResFactory resFactory) {
        this.testRepository = testRepository;
        this.resFactory = resFactory;
    }

    @Override
    @Transactional
    public RestResponse<TestResponse> getTest(String id) {
        Optional<TestResponse> res = this.testRepository.getTestResponse(UUID.fromString(id));

        if (!res.isPresent()) {
            throw new InvalidDataException(resFactory.fail(TestResMsg.TEST_NOT_FOUND));
        }
        
        return resFactory.success(TestResMsg.TEST_CREATE_SUCCESS, res.get());
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
