package overcloud.blog.usecase.test.get_practice.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.PracticeEntity;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.usecase.test.common.PracticeError;
import overcloud.blog.usecase.test.get_practice.GetPracticeResultService;
import overcloud.blog.usecase.test.get_practice.PracticeQuestion;
import overcloud.blog.usecase.test.get_practice.PracticeResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GetPracticeResultServiceImpl implements GetPracticeResultService {
    private final IPracticeRepository practiceRepository;

    GetPracticeResultServiceImpl(IPracticeRepository practiceRepository) {
        this.practiceRepository = practiceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PracticeResult getPracticeResult(UUID practiceId) {
//        PracticeEntity practiceEntity = practiceRepository.findById(practiceId)
//                .orElseThrow(() -> new InvalidDataException(PracticeError.PRACTICE_NOT_FOUND));
//        TestEntity testEntity = practiceEntity.getTest();
        return practiceRepository.getPracticeResult(practiceId);
//        List<PracticeQuestion> questions = new ArrayList<>();
//        List<QuestionEntity> questionEntities = testEntity.getQuestions();
//        for (QuestionEntity question : questionEntities) {
//            question.getQuestion();
//            question.getQuestionType();
//            List<AnswerEntity> answerEntities = question.getAnswers();
//            for (AnswerEntity answerEntity : answerEntities) {
////                answerEntity.get
//            }
//
//        }
//        practiceEntity.getChoices();
//        practiceEntity.getEssayAnswers();
//        return null;
    }

}
