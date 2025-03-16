package overcloud.blog.repository.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import overcloud.blog.entity.ChoiceAnswerEntity;
import overcloud.blog.repository.jparepository.JpaChoiceAnswerRepository;

@Repository
public class ChoiceAnswerRepository implements overcloud.blog.repository.ChoiceAnswerRepository {
    private final JpaChoiceAnswerRepository jpa;

    @PersistenceContext
    private final EntityManager entityManager;

    public ChoiceAnswerRepository(JpaChoiceAnswerRepository jpa, EntityManager entityManager) {
        this.jpa = jpa;
        this.entityManager = entityManager;
    }

    @Override
    public void saveAll(List<ChoiceAnswerEntity> answers) {
        jpa.saveAll(answers);
    }

    @Override
    public void updateAll(List<ChoiceAnswerEntity> answers) {
        if (answers == null || answers.isEmpty()) {
            return;
        }
        this.jpa.saveAll(answers);
    }

    @Override
    public void deleteAll(List<UUID> answerIds) {
        jpa.deleteAll(answerIds);
    }
}

