package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.PracticeChoiceAnswerEntity;
import overcloud.blog.entity.PracticeChoiceAnswerId;

@Repository
public interface JpaPracticeChoiceAnswerRepository extends JpaRepository<PracticeChoiceAnswerEntity, PracticeChoiceAnswerId> {

}
