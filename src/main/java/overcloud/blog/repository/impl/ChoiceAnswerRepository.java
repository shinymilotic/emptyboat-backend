package overcloud.blog.repository.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import overcloud.blog.entity.AnswerEntity;
import overcloud.blog.repository.IChoiceAnswerRepository;
import overcloud.blog.repository.jparepository.JpaChoiceAnswerRepository;

@Repository
public class ChoiceAnswerRepository implements IChoiceAnswerRepository {
    
    private final JpaChoiceAnswerRepository jpa;
    
    public ChoiceAnswerRepository(JpaChoiceAnswerRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void saveAll(List<AnswerEntity> answers) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public void updateAll(List<AnswerEntity> answers) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAll'");
    }

    @Override
    public void deleteAll(List<UUID> answerIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    
}
