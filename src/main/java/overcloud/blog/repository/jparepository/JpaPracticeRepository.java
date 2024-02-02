package overcloud.blog.repository.jparepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.PracticeEntity;

@Repository
public interface JpaPracticeRepository extends JpaRepository<PracticeEntity, UUID> {
    @Query(" SELECT * FROM PracticeEntity p INNER JOIN p.test  WHERE p.testerId = :testerId ")
    List<PracticeEntity> findByTesterId(@Param("testerId") UUID testerId);

    @Modifying
    @Query(" DELETE FROM PracticeEntity p WHERE p.testId = :testId ")
    void deleteByTestId(@Param("testId") UUID testId);
}
