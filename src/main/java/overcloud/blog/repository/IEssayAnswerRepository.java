package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;

import overcloud.blog.entity.PracticeOpenQuestionEntity;

public interface IEssayAnswerRepository {
    void saveAll(List<PracticeOpenQuestionEntity> essayAnswerEntities);
    void deleteAllByQuestionId(List<UUID> questionIds);
}
