package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.TestEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaTestRepository extends JpaRepository<TestEntity, UUID> {
    @Query("SELECT t FROM TestEntity t WHERE t.testId = :testId")
    Optional<TestEntity> findById(@Param("testId") UUID testId);

    @Modifying
    @Query("UPDATE TestEntity t SET t.title = :title, t.description = :description WHERE t.testId = :testId")
    void updateTest(@Param("testId") UUID testId, @Param("title") String title, @Param("description") String description);

    @Query("SELECT t FROM TestEntity t WHERE t.authorId = :userId")
    List<TestEntity> findByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("DELETE FROM TestEntity t WHERE t.authorId = :userId")
    void deleteByUserId(@Param("userId") UUID userId);
}

