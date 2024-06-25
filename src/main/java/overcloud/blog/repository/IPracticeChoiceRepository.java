package overcloud.blog.repository;

import java.util.List;

import overcloud.blog.entity.PracticeChoiceEntity;

public interface IPracticeChoiceRepository {
    void saveAll(List<PracticeChoiceEntity> choiceEntities);
}
