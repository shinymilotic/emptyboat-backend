package overcloud.blog.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeChoiceRepository extends JpaRepository<PracticeChoiceRepository, UUID>{
    
}
