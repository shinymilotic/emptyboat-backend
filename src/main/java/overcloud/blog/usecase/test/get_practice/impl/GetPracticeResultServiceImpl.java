package overcloud.blog.usecase.test.get_practice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.PracticeEntity;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.usecase.test.common.PracticeError;
import overcloud.blog.usecase.test.get_practice.GetPracticeResultService;
import overcloud.blog.usecase.test.get_practice.PracticeQuestion;
import overcloud.blog.usecase.test.get_practice.PracticeResult;

@Service
public class GetPracticeResultServiceImpl implements GetPracticeResultService {
    private final IPracticeRepository practiceRepository;

    GetPracticeResultServiceImpl(IPracticeRepository practiceRepository) {
        this.practiceRepository = practiceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PracticeResult getPracticeResult(String practiceId) {
        PracticeEntity practiceEntity = practiceRepository.findById(UUID.fromString(practiceId))
                .orElseThrow(() -> new InvalidDataException(PracticeError.PRACTICE_NOT_FOUND));
        TestEntity testEntity = practiceEntity.getTest();
        List<Object> object = practiceRepository.getPracticeResult(practiceId);
        List<PracticeQuestion> questions = new ArrayList<>();
        List<QuestionEntity> questionEntities = testEntity.getQuestions();
        practiceEntity.getChoices();
        practiceEntity.getEssayAnswers();
        return PracticeResult.builder()
            .practiceId(practiceId)
            .testTitle(testEntity.getTitle())
            .questions(questions)
            .build();
    }
    
}
