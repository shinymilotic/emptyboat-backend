package overcloud.blog.application.practice.get_practice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import overcloud.blog.application.practice.core.PracticeError;
import overcloud.blog.application.practice.get_practice.GetPracticeResultService;
import overcloud.blog.application.practice.get_practice.PracticeQuestion;
import overcloud.blog.application.practice.get_practice.PracticeResult;
import overcloud.blog.entity.PracticeEntity;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.repository.PracticeRepository;

@Service
public class GetPracticeResultServiceImpl implements GetPracticeResultService{

    private PracticeRepository practiceRepository;

    GetPracticeResultServiceImpl(PracticeRepository practiceRepository) {
        this.practiceRepository = practiceRepository;
    }

    @Override
    public PracticeResult getPracticeResult(String practiceId) {
        PracticeEntity practiceEntity = practiceRepository.findById(UUID.fromString(practiceId))
                .orElseThrow(() -> new InvalidDataException(PracticeError.PRACTICE_NOT_FOUND));
        TestEntity testEntity = practiceEntity.getTest();
        
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
