package overcloud.blog.repository.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import overcloud.blog.entity.PracticeOpenAnswerEntity;
import overcloud.blog.repository.IPracticeOpenAnswerRepository;
import overcloud.blog.repository.jparepository.JpaPracticeOpenAnswerRepository;

@Repository
public class PracticeOpenAnswerRepositoryImpl implements IPracticeOpenAnswerRepository {
    private final JpaPracticeOpenAnswerRepository jpa;

    public PracticeOpenAnswerRepositoryImpl(JpaPracticeOpenAnswerRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void saveAll(List<PracticeOpenAnswerEntity> openAnswerEntities) {
        jpa.saveAll(openAnswerEntities);
    }

    @Override
    public void deleteAllByQuestionId(List<UUID> questionIds) {
        jpa.deleteAllByQuestionId(questionIds);
    }
}
