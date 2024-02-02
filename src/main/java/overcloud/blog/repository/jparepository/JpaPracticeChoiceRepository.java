package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.PracticeChoiceEntity;

import java.util.UUID;

@Repository
public interface JpaPracticeChoiceRepository extends JpaRepository<PracticeChoiceEntity, UUID> {

}
