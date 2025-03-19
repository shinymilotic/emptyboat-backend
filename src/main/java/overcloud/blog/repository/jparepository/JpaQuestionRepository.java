package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.QuestionEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaQuestionRepository extends JpaRepository<QuestionEntity, UUID> {
    @Modifying
    @Query("DELETE FROM QuestionEntity q WHERE q.testId = :testId")
    void deleteByTestId(@Param("testId") UUID testId);

    @Query("SELECT q.questionId FROM QuestionEntity q WHERE q.testId = :testId")
    List<UUID> findByTestId(@Param("testId") UUID testId);
}
