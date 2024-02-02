package overcloud.blog.repository;

import overcloud.blog.entity.PracticeEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPracticeRepository {
    List<PracticeEntity> findByTesterId(UUID testerId);

    void deleteByTestId(UUID testId);

    Optional<PracticeEntity> findById(UUID id);

    List<Object> getPracticeResult(String id);

    PracticeEntity save(PracticeEntity entity);
}
