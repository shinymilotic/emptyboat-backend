package overcloud.blog.repository.a;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import overcloud.blog.entity.PracticeChoiceEntity;

public interface PracticeChoiceRepository extends JpaRepository<PracticeChoiceEntity, UUID>{
    
}
