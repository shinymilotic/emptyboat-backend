package overcloud.blog.repository;

import java.util.List;
import overcloud.blog.entity.TestQuestion;

public interface ITestQuestionRepository {
    void saveAll(List<TestQuestion> testQuestions);
}
