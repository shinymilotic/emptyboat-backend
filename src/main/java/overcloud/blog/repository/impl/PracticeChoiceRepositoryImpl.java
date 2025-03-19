package overcloud.blog.repository.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import overcloud.blog.entity.PracticeChoiceAnswerEntity;
import overcloud.blog.repository.PracticeChoiceRepository;
import overcloud.blog.repository.jparepository.JpaPracticeChoiceAnswerRepository;

@Repository
public class PracticeChoiceRepositoryImpl implements PracticeChoiceRepository {
    private final JpaPracticeChoiceAnswerRepository jpa;

    public PracticeChoiceRepositoryImpl(JpaPracticeChoiceAnswerRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void saveAll(List<PracticeChoiceAnswerEntity> choiceEntities) {
        jpa.saveAll(choiceEntities);
    }

    @Override
    public void deleteAll(List<UUID> choiceAnswerIds) {
        jpa.findByChoiceAnswerIds(choiceAnswerIds);
    }

    @Override
    public void deleteByPracticeIdList(List<UUID> practiceIdList) {
        jpa.deleteByPracticeIdList(practiceIdList);
    }
}
