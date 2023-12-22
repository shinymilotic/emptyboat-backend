package overcloud.blog.usecase.practice;

import org.springframework.context.annotation.DependsOn;
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

    public PracticeRepositoryImpl(JpaPracticeRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<PracticeEntity> findByTesterId(UUID testerId) {
        return jpa.findByTesterId(testerId);
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
