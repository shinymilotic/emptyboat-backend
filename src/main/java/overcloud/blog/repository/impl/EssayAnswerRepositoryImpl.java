package overcloud.blog.repository.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import overcloud.blog.entity.PracticeOpenQuestionEntity;
import overcloud.blog.repository.IPracticeOpenQuestionRepository;
import overcloud.blog.repository.jparepository.JpaPracticeOpenQuestionRepository;

@Repository
public class EssayAnswerRepositoryImpl implements IPracticeOpenQuestionRepository {
    private final JpaPracticeOpenQuestionRepository jpa;

    public EssayAnswerRepositoryImpl(JpaPracticeOpenQuestionRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void saveAll(List<PracticeOpenQuestionEntity> essayAnswerEntities) {
        jpa.saveAll(essayAnswerEntities);
    }

    @Override
    public void deleteAllByQuestionId(List<UUID> questionIds) {
        jpa.deleteAllByQuestionId(questionIds);
    }
}
