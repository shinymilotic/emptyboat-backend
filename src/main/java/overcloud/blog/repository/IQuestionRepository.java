package overcloud.blog.repository;

import overcloud.blog.entity.QuestionEntity;

import java.util.List;
import java.util.UUID;

public interface IQuestionRepository {
    QuestionEntity save(QuestionEntity entity);
    void saveAll(List<QuestionEntity> questions);
    void updateAll(List<QuestionEntity> questions);
    void deleteAll(List<UUID> questionIds);
    void deleteByTestId(UUID testId);
}
