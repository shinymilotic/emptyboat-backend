package overcloud.blog.repository.jparepository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.TestEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaTestRepository extends JpaRepository<TestEntity, UUID> {
    @Query("SELECT t FROM TestEntity t WHERE t.id = :id")
    Optional<TestEntity> findById(@Param("id") String id);

    @Modifying
    @Query("UPDATE TestEntity t SET t.title = :title AND t.description = :description WHERE t.id = :id")
    void updateTest(@Param("id") UUID id, @Param("title") String title, @Param("description") String description);
}

