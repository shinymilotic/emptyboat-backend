package overcloud.blog.repository;

import overcloud.blog.entity.TestEntity;
import overcloud.blog.usecase.test.get_list_test.TestListRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITestRepository {
    List<TestListRecord> findAll();
    Optional<TestEntity> findById(UUID slug);
    TestEntity save(TestEntity testEntity);
    void deleteById(UUID id);
}
