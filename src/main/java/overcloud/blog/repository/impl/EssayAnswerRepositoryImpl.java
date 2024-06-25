package overcloud.blog.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import overcloud.blog.entity.EssayAnswerEntity;
import overcloud.blog.repository.IEssayAnswerRepository;
import overcloud.blog.repository.jparepository.JpaEssayAnswerRepository;

@Repository
public class EssayAnswerRepositoryImpl implements IEssayAnswerRepository {
    private final JpaEssayAnswerRepository jpa;

    public EssayAnswerRepositoryImpl(JpaEssayAnswerRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void saveAll(List<EssayAnswerEntity> essayAnswerEntities) {
        jpa.saveAll(essayAnswerEntities);
    }
}
