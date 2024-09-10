package overcloud.blog.repository.jparepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import overcloud.blog.entity.TestQuestion;
import overcloud.blog.entity.TestQuestionId;

@Repository
public interface JpaTestQuestionRepository extends JpaRepository<TestQuestion, TestQuestionId> {
    @Modifying
    @Query("DELETE FROM TestQuestion t WHERE t.id.questionId IN :questionIds")
    void deleteByQuestionIds(List<UUID> questionIds);
}
