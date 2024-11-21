package overcloud.blog.repository.jparepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.PracticeChoiceAnswerEntity;
import overcloud.blog.entity.PracticeChoiceAnswerId;

@Repository
public interface JpaPracticeChoiceAnswerRepository extends JpaRepository<PracticeChoiceAnswerEntity, PracticeChoiceAnswerId> {
    @Modifying
    @Query("DELETE FROM PracticeChoiceAnswerEntity p WHERE p.practiceChoiceAnswerId.choiceAnswerId = :answerIds")
    void deleteByChoiceAnswerId(@Param("answerIds") List<UUID> answerIds);
}
