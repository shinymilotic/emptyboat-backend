package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;

import overcloud.blog.entity.PracticeChoiceAnswerEntity;

public interface PracticeChoiceRepository {
    void saveAll(List<PracticeChoiceAnswerEntity> choiceEntities);
    void deleteAll(List<UUID> choiceAnswerIds);
    void deleteByPracticeIdList(List<UUID> practiceIdList);
}
