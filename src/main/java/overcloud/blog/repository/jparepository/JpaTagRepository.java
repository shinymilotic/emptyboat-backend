package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.TagEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaTagRepository extends JpaRepository<TagEntity, UUID> {
    @Query("SELECT tag FROM TagEntity tag WHERE tag.name IN (:tagList)")
    List<TagEntity> findByTagName(List<String> tagList);
}
