package overcloud.blog.repository.impl;

import org.springframework.stereotype.Repository;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.repository.IQuestionRepository;
import overcloud.blog.repository.jparepository.JpaQuestionRepository;

import java.util.List;
import java.util.UUID;

@Repository
public class QuestionRepositoryImpl implements IQuestionRepository {

    private final JpaQuestionRepository jpaQuestionRepository;

    public QuestionRepositoryImpl(JpaQuestionRepository jpaQuestionRepository) {
        this.jpaQuestionRepository = jpaQuestionRepository;
    }

    @Override
    public QuestionEntity save(QuestionEntity entity) {
        return jpaQuestionRepository.save(entity);
    }

    @Override
    public void saveAll(List<QuestionEntity> entityList) {
        jpaQuestionRepository.saveAll(entityList);
    }

    @Override
    public void updateAll(List<QuestionEntity> questions) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAll'");
    }

    @Override
    public void deleteAll(List<UUID> questionIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }
}
