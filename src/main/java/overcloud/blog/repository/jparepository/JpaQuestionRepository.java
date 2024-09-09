package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.QuestionEntity;
import java.util.UUID;

@Repository
public interface JpaQuestionRepository extends JpaRepository<QuestionEntity, UUID> {


}
