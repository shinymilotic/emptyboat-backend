package overcloud.blog.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import overcloud.blog.entity.PracticeChoiceQuestionEntity;
import overcloud.blog.repository.IPracticeChoiceRepository;
import overcloud.blog.repository.jparepository.JpaPracticeChoiceRepository;

@Repository
public class PracticeChoiceRepositoryImpl implements IPracticeChoiceRepository {
    private final JpaPracticeChoiceRepository jpa;

    public PracticeChoiceRepositoryImpl(JpaPracticeChoiceRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void saveAll(List<PracticeChoiceQuestionEntity> choiceEntities) {
        jpa.saveAll(choiceEntities);
    }
}
