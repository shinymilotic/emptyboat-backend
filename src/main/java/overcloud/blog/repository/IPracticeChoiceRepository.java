package overcloud.blog.repository;

import java.util.List;

import overcloud.blog.entity.PracticeChoiceQuestionEntity;

public interface IPracticeChoiceRepository {
    void saveAll(List<PracticeChoiceQuestionEntity> choiceEntities);
}
