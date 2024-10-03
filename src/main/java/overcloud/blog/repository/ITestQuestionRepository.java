package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;

import overcloud.blog.entity.TestQuestion;

public interface ITestQuestionRepository {
    void saveAll(List<TestQuestion> testQuestions);
    void deleteAllById(List<UUID> ids);
    void deleteByTestId(UUID testId);
}
