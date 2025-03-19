package overcloud.blog.repository.impl;

import org.springframework.stereotype.Repository;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.repository.QuestionRepository;
import overcloud.blog.repository.jparepository.JpaQuestionRepository;

import java.util.List;
import java.util.UUID;

@Repository
public class QuestionRepositoryImpl implements QuestionRepository {
    private final JpaQuestionRepository jpa;

    public QuestionRepositoryImpl(JpaQuestionRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public QuestionEntity save(QuestionEntity entity) {
        return jpa.save(entity);
    }

    @Override
    public void saveAll(List<QuestionEntity> entityList) {
        jpa.saveAll(entityList);
    }

    @Override
    public void updateAll(List<QuestionEntity> questions) {
        jpa.saveAllAndFlush(questions);
    }

    @Override
    public void deleteAll(List<UUID> questionIds) {
        jpa.deleteAllByIdInBatch(questionIds);
    }

    @Override
    public void deleteByTestId(UUID testId) {
        jpa.deleteByTestId(testId);
    }

    @Override
    public List<UUID> findByTestId(UUID testId) {
        return jpa.findByTestId(testId);
    }
}
