package overcloud.blog.repository.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.PracticeEntity;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.repository.PracticeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PracticeRepositoryImpl implements IPracticeRepository {
    private final PracticeRepository practiceRepository;

    public PracticeRepositoryImpl(PracticeRepository practiceRepository) {
        this.practiceRepository = practiceRepository;
    }

    @Override
    public List<PracticeEntity> findByTesterId(UUID testerId) {
        return practiceRepository.findByTesterId(testerId);
    }

    @Override
    public void deleteByTestId(UUID testId) {
        practiceRepository.deleteByTestId(testId);
    }

    @Override
    public Optional<PracticeEntity> findById(UUID id) {
        return practiceRepository.findById(id);
    }

    @Override
    public List<Object> getPracticeResult(String id) {
        return new ArrayList<>();
    }

    @Override
    public PracticeEntity save(PracticeEntity entity) {
        return practiceRepository.save(entity);
    }
}
