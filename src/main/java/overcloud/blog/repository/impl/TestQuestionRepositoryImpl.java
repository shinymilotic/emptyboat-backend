package overcloud.blog.repository.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import overcloud.blog.entity.TestQuestion;
import overcloud.blog.repository.ITestQuestionRepository;
import overcloud.blog.repository.jparepository.JpaTestQuestionRepository;

@Repository
public class TestQuestionRepositoryImpl implements ITestQuestionRepository {

    private final JpaTestQuestionRepository jpa;

    public TestQuestionRepositoryImpl(JpaTestQuestionRepository jpa) {
        this.jpa = jpa;
    }
    
    @Override
    public void saveAll(List<TestQuestion> testQuestions) {
        jpa.saveAll(testQuestions);
    }

    @Override
    public void deleteAllById(List<UUID> ids) {
        jpa.deleteByQuestionIds(ids);
    }

    @Override
    public void deleteByTestId(UUID testId) {
        jpa.deleteByTestId(testId);
    }
    
}
