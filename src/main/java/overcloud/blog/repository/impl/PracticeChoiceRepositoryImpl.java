package overcloud.blog.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import overcloud.blog.entity.PracticeChoiceAnswerEntity;
import overcloud.blog.repository.IPracticeChoiceRepository;
import overcloud.blog.repository.jparepository.JpaPracticeChoiceAnswerRepository;

@Repository
public class PracticeChoiceRepositoryImpl implements IPracticeChoiceRepository {
    private final JpaPracticeChoiceAnswerRepository jpa;

    public PracticeChoiceRepositoryImpl(JpaPracticeChoiceAnswerRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void saveAll(List<PracticeChoiceAnswerEntity> choiceEntities) {
        jpa.saveAll(choiceEntities);
    }
}
