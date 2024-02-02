package overcloud.blog.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.PracticeEntity;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.repository.jparepository.JpaPracticeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PracticeRepositoryImpl implements IPracticeRepository {
    private final JpaPracticeRepository jpa;
    private final EntityManager entityManager;

    public PracticeRepositoryImpl(JpaPracticeRepository jpa, EntityManager entityManager) {
        this.jpa = jpa;
        this.entityManager = entityManager;
    }

    @Override
    public List<PracticeEntity> findByTesterId(UUID testerId) {
        TypedQuery<PracticeEntity> practiceQuery = entityManager
                .createQuery("SELECT p FROM PracticeEntity p JOIN FETCH p.test  WHERE p.testerId = :testerId ORDER BY p.createdAt DESC ",
                        PracticeEntity.class)
                .setParameter("testerId", testerId);

        return practiceQuery.getResultList();
    }

    @Override
    public void deleteByTestId(UUID testId) {
        jpa.deleteByTestId(testId);
    }

    @Override
    public Optional<PracticeEntity> findById(UUID id) {
        return jpa.findById(id);
    }

    @Override
    public List<Object> getPracticeResult(String id) {
        return new ArrayList<>();
    }

    @Override
    public PracticeEntity save(PracticeEntity entity) {
        return jpa.save(entity);
    }
}
