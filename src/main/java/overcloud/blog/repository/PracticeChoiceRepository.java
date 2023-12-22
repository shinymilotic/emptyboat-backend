package overcloud.blog.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import overcloud.blog.entity.PracticeChoiceEntity;

public interface PracticeChoiceRepository extends JpaRepository<PracticeChoiceEntity, UUID>{
    
}
