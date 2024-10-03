package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;

import overcloud.blog.entity.EssayAnswerEntity;

public interface IEssayAnswerRepository {
    void saveAll(List<EssayAnswerEntity> essayAnswerEntities);
    void deleteAllByQuestionId(List<UUID> questionIds);
    void deleteByPracticeId(Object object);
}
