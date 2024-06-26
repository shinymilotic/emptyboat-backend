package overcloud.blog.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.repository.jparepository.JpaTestRepository;
import overcloud.blog.usecase.test.get_list_test.TestListRecord;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TestRepositoryImpl implements ITestRepository {
    private final JpaTestRepository jpa;
    private final EntityManager entityManager;

    public TestRepositoryImpl(JpaTestRepository jpaTestRepository, EntityManager entityManager) {
        this.jpa = jpaTestRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<TestListRecord> findAll() {
        String s = "SELECT new overcloud.blog.usecase.test.get_list_test.TestListRecord(t.title, t.description, t.slug) " +
                " FROM TestEntity t ";
        TypedQuery<TestListRecord> testQuery = entityManager.createQuery(s, TestListRecord.class);
        return testQuery.getResultList();
    }

    @Override
    public Optional<TestEntity> findBySlug(String slug) {
        return jpa.findBySlug(slug);
    }

    @Override
    public TestEntity save(TestEntity testEntity) {
        return jpa.save(testEntity);
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
