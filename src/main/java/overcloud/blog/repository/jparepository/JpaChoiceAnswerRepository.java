package overcloud.blog.repository.jparepository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.ChoiceAnswerEntity;

@Repository
public interface JpaChoiceAnswerRepository extends JpaRepository<ChoiceAnswerEntity, UUID> {
    @Modifying
    @Query("DELETE FROM ChoiceAnswerEntity a WHERE a.choiceAnswerId IN :answerIds")
    void deleteAll(List<UUID> answerIds);

    @Modifying
    @Query("DELETE FROM ChoiceAnswerEntity a WHERE a.questionId IN :questionIdList")
    void deleteByQuestionIdList(List<UUID> questionIdList);
}
