package overcloud.blog.repository.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
        LocalDateTime now = LocalDateTime.now();
        StringBuilder query = new StringBuilder();

        query.append("UPDATE choice_answers FROM (VALUES ");

        for (AnswerEntity answerEntity : answers) {
            query.append("(");
            query.append(answerEntity.getAnswer());
            query.append(", ");
            query.append(answerEntity.isTruth()); 
            query.append(", ");
            query.append(now);
            query.append(")");
        }
        this.entityManager.createNativeQuery(query.toString()).executeUpdate();
    }

    @Override
    public void deleteAll(List<UUID> answerIds) {
        jpa.deleteAll(answerIds);
    }

    
}

