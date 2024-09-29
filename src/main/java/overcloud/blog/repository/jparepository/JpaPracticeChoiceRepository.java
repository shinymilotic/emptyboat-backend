package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.PracticeChoiceQuestionEntity;
import overcloud.blog.entity.PracticeChoiceQuestionId;

@Repository
public interface JpaPracticeChoiceRepository extends JpaRepository<PracticeChoiceQuestionEntity, PracticeChoiceQuestionId> {

}
