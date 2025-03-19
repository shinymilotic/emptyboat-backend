package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;
import overcloud.blog.entity.PracticeOpenAnswerEntity;

public interface PracticeOpenAnswerRepository {
    void saveAll(List<PracticeOpenAnswerEntity> practiceOpenQuestions);
    void deleteAllByQuestionId(List<UUID> questionIds);
    void deleteByPracticeIdList(List<UUID> practiceIdList);
}
