package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;

import overcloud.blog.entity.ChoiceAnswerEntity;

public interface IChoiceAnswerRepository {
    void saveAll(List<ChoiceAnswerEntity> answers);
    void updateAll(List<ChoiceAnswerEntity> answers);
    void deleteAll(List<UUID> answerIds);
}
