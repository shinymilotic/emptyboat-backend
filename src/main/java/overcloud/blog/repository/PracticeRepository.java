package overcloud.blog.repository;

import overcloud.blog.entity.PracticeEntity;
import overcloud.blog.usecase.test.get_practice.PracticeResult;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import jakarta.persistence.Tuple;

public interface PracticeRepository {
    List<Tuple> findByTesterId(UUID testerId);
    Optional<PracticeEntity> findById(UUID id);
    PracticeResult getPracticeResult(UUID id);
    PracticeEntity save(PracticeEntity entity);
    void deleteByTestId(UUID testId);
    List<UUID> findByTestId(UUID testId);
}
