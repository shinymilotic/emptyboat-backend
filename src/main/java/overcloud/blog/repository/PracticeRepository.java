package overcloud.blog.repository;
import java.util.List;
import java.util.UUID;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;
import overcloud.blog.entity.PracticeEntity;

@Repository
public interface PracticeRepository extends JpaRepository<PracticeEntity, UUID> {
    @Query(" SELECT * FROM PracticeEntity JOIN FETCH test p WHERE p.testerId = :testerId ORDER BY p.createdAt DESC ")
    List<PracticeEntity> findByTesterId(@Param("testerId") UUID testerId);

    @Modifying
    @Query(" DELETE FROM PracticeEntity p WHERE p.testId = :testId ")
    void deleteByTestId(@Param("testId") UUID testId);

    List<Object> getPracticeResult(String practiceId);
}
