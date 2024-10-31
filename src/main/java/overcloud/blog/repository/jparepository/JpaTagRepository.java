package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.Tuple;
import overcloud.blog.entity.TagEntity;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaTagRepository extends JpaRepository<TagEntity, UUID> {
    @Query("SELECT tag FROM TagEntity tag WHERE tag.id IN (:tagList)")
    List<TagEntity> findByTagIds(List<String> tagList);

    @Query("SELECT tag.tagId, tag.name, user.userId FROM TagEntity tag LEFT JOIN UserEntity user ON user.userId = tag.tagId WHERE user.userId = :userId")
    List<Tuple> findAllWithFollowing(@Param("userId") UUID userId);
}
