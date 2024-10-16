package overcloud.blog.repository.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import overcloud.blog.entity.AnswerEntity;
import overcloud.blog.repository.IChoiceAnswerRepository;
import overcloud.blog.repository.jparepository.JpaChoiceAnswerRepository;

@Repository
public class ChoiceAnswerRepository implements IChoiceAnswerRepository {
    
    private final JpaChoiceAnswerRepository jpa;

    @PersistenceContext
    private final EntityManager entityManager;

    public ChoiceAnswerRepository(JpaChoiceAnswerRepository jpa, EntityManager entityManager) {
        this.jpa = jpa;
        this.entityManager = entityManager;
    }

    @Override
    public void saveAll(List<AnswerEntity> answers) {
        jpa.saveAll(answers);
    }

    @Override
    public void updateAll(List<AnswerEntity> answers) {
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

