package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;
import overcloud.blog.entity.PracticeOpenQuestionEntity;

public interface IPracticeOpenQuestionRepository {
    void saveAll(List<PracticeOpenQuestionEntity> practiceOpenQuestions);
    void deleteAllByQuestionId(List<UUID> questionIds);
}
