package overcloud.blog.repository;

import overcloud.blog.entity.TestEntity;
import overcloud.blog.usecase.test.get_test.response.TestResponse;
import overcloud.blog.usecase.test.get_list_test.TestListRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestRepository {
    List<TestListRecord> findAll();
    Optional<TestEntity> findById(UUID id);
    TestEntity save(TestEntity testEntity);
    void deleteById(UUID id);
    Optional<TestResponse> getTestResponse(UUID id);
    void updateTest(UUID testId, String title, String description);
    List<TestEntity> getProfileTest(UUID userId);
    void deleteByUserId(UUID userId);
}
