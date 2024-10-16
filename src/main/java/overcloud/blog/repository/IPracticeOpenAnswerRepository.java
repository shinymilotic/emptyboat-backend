package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;
import overcloud.blog.entity.PracticeOpenAnswerEntity;

public interface IPracticeOpenAnswerRepository {
    void saveAll(List<PracticeOpenAnswerEntity> practiceOpenQuestions);
    void deleteAllByQuestionId(List<UUID> questionIds);
}
