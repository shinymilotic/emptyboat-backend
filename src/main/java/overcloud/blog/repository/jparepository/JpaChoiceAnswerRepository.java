package overcloud.blog.repository.jparepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.AnswerEntity;

@Repository
public interface JpaChoiceAnswerRepository extends JpaRepository<AnswerEntity, UUID> {
    @Modifying
    @Query("DELETE FROM AnswerEntity a WHERE a.id IN :answerIds")
    void deleteAll(List<UUID> answerIds);
}
