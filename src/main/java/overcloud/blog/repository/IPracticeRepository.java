package overcloud.blog.repository.a;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.jpa.repository.Modifying;
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
