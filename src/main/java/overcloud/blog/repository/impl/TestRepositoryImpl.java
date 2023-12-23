package overcloud.blog.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.repository.jparepository.JpaTestRepository;
import overcloud.blog.usecase.test.get_list_test.TestListRecord;

import java.util.*;

@Repository
public class TestRepositoryImpl implements ITestRepository {
    private final JpaTestRepository jpaTestRepository;

    private final EntityManager entityManager;

    public TestRepositoryImpl(JpaTestRepository jpaTestRepository, EntityManager entityManager) {
        this.jpaTestRepository = jpaTestRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<TestListRecord> findAll() {
        String s = "SELECT new overcloud.blog.usecase.test.get_list_test.TestListRecord(t.title, t.slug) " +
                " FROM TestEntity t ";
        TypedQuery<TestListRecord> testQuery = entityManager.createQuery(s, TestListRecord.class);
        return testQuery.getResultList();
    }
}
