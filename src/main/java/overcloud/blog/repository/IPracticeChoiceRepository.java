package overcloud.blog.repository;

import java.util.List;

import overcloud.blog.entity.PracticeChoiceAnswerEntity;

public interface IPracticeChoiceRepository {
    void saveAll(List<PracticeChoiceAnswerEntity> choiceEntities);
}
