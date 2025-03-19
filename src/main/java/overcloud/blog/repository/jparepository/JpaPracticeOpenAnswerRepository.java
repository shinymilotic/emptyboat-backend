package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.PracticeOpenAnswerEntity;
import overcloud.blog.entity.PracticeOpenAnswerId;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaPracticeOpenAnswerRepository extends JpaRepository<PracticeOpenAnswerEntity, PracticeOpenAnswerId> {
    @Modifying
    @Query("DELETE FROM PracticeOpenAnswerEntity a WHERE a.id.questionId IN :questionIds")
    void deleteAllByQuestionId(List<UUID> questionIds);

    @Modifying
    @Query("DELETE FROM PracticeOpenAnswerEntity a WHERE a.id.practiceId IN :practiceIdList")
    void deleteByPracticeIdList(List<UUID> practiceIdList);
}
