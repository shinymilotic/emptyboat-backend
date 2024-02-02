package overcloud.blog.repository.jparepository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import overcloud.blog.entity.EssayAnswerEntity;

@Repository
public interface JpaEssayAnswerRepository extends JpaRepository<EssayAnswerEntity, UUID> {

}
