package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.EssayAnswerEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaEssayAnswerRepository extends JpaRepository<EssayAnswerEntity, UUID> {
    @Modifying
    @Query("DELETE FROM EssayAnswerEntity a WHERE a.questionId IN :questionIds")
    void deleteAllByQuestionId(List<UUID> questionIds);
}
