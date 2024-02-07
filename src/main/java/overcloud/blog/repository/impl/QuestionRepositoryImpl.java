package overcloud.blog.repository.impl;

import org.springframework.stereotype.Repository;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.repository.IQuestionRepository;
import overcloud.blog.repository.jparepository.JpaQuestionRepository;

import java.util.List;

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
    public List<QuestionEntity> saveAll(List<QuestionEntity> entityList) {
        return jpaQuestionRepository.saveAll(entityList);
    }
}
