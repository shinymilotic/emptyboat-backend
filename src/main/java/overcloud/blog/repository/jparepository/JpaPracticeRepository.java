package overcloud.blog.repository.jparepository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.PracticeEntity;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaPracticeRepository extends JpaRepository<PracticeEntity, UUID> {
    @Modifying
    @Query("DELETE FROM PracticeEntity p WHERE p.testId = :testId ")
    void deleteByTestId(@Param("testId") UUID testId);

    @Query("SELECT p.practiceId FROM PracticeEntity p WHERE p.testId = :testId")
    List<UUID> findByTestId(@Param("testId") UUID testId);
}
