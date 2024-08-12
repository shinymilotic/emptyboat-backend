package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;

import overcloud.blog.entity.AnswerEntity;

public interface IChoiceAnswerRepository {
    void saveAll(List<AnswerEntity> answers);
    void updateAll(List<AnswerEntity> answers);
    void deleteAll(List<UUID> answerIds);
}
