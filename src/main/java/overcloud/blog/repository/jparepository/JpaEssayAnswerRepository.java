package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.EssayAnswerEntity;

import java.util.UUID;

@Repository
public interface JpaEssayAnswerRepository extends JpaRepository<EssayAnswerEntity, UUID> {

}
