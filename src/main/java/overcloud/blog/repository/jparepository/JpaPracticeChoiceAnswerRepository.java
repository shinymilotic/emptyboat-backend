package overcloud.blog.repository.jparepository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.PracticeChoiceAnswerEntity;
import overcloud.blog.entity.PracticeChoiceAnswerId;

@Repository
public interface JpaPracticeChoiceAnswerRepository extends JpaRepository<PracticeChoiceAnswerEntity, PracticeChoiceAnswerId> {
    @Modifying
    @Query("DELETE FROM PracticeChoiceAnswerEntity p WHERE p.id.choiceAnswerId IN :answerIds")
    void findByChoiceAnswerIds(List<UUID> answerIds);

    @Modifying
    @Query("DELETE FROM PracticeChoiceAnswerEntity p WHERE p.id.practiceId IN :practiceIdList")
    void deleteByPracticeIdList(List<UUID> practiceIdList);
}
