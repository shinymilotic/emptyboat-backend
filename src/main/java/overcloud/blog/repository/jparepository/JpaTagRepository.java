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

    @Query(value = "SELECT t.tag_id, t.name, u1.user_id FROM tags t  " +
            " LEFT JOIN tag_follows tf ON tf.tag_id = t.tag_id" +
            " LEFT JOIN (SELECT u0.user_id FROM users u0 WHERE u0.user_id = :userId) u1 ON u1.user_id = tf.follower_id ", nativeQuery = true)
    List<Tuple> findAllWithFollowing(@Param("userId") UUID userId);

    @Query(value = "SELECT t.tag_id, t.name FROM tags t  " +
            " INNER JOIN tag_follows tf ON tf.tag_id = t.tag_id" +
            " INNER JOIN (SELECT u0.user_id FROM users u0 WHERE u0.user_id = :userId) u1 ON u1.user_id = tf.follower_id ", nativeQuery = true)
    List<TagEntity> findFollowingTags(@Param("userId") UUID userId);

    @Query(value = "SELECT tag_id, name FROM tags LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<TagEntity> findTags(int limit, int offset);
}
