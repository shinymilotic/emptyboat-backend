package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;

import overcloud.blog.entity.PracticeChoiceAnswerEntity;

public interface IPracticeChoiceRepository {
    void saveAll(List<PracticeChoiceAnswerEntity> choiceEntities);
    void deleteAll(List<UUID> choiceAnswerIds);
}
