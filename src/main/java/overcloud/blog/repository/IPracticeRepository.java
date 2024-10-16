package overcloud.blog.repository;

import overcloud.blog.entity.PracticeEntity;
import overcloud.blog.usecase.test.get_practice.PracticeResult;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import jakarta.persistence.Tuple;

public interface IPracticeRepository {
    List<Tuple> findByTesterId(UUID testerId);
    void deleteByTestId(UUID testId);
    Optional<PracticeEntity> findById(UUID id);
    PracticeResult getPracticeResult(UUID id);
    PracticeEntity save(PracticeEntity entity);
    int deleteTestId(UUID testId);
}
