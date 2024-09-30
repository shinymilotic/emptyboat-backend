package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.PracticeOpenQuestionEntity;
import overcloud.blog.entity.PracticeOpenQuestionId;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaPracticeOpenQuestionRepository extends JpaRepository<PracticeOpenQuestionEntity, PracticeOpenQuestionId> {
    @Modifying
    @Query("DELETE FROM PracticeOpenQuestionEntity a WHERE a.id.questionId IN :questionIds")
    void deleteAllByQuestionId(List<UUID> questionIds);
}
