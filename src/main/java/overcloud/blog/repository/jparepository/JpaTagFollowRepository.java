package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.TagFollowEntity;
import overcloud.blog.entity.TagFollowId;

import java.util.UUID;

@Repository
public interface JpaTagFollowRepository extends JpaRepository<TagFollowEntity, TagFollowId> {
    @Modifying
    @Query("DELETE FROM TagFollowEntity t WHERE t.tagFollowId.followerId = :userId")
    void deleteByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("DELETE FROM TagFollowEntity t WHERE t.tagFollowId.tagId = :tagId")
    void deleteByTagId(@Param("tagId") UUID tagId);
}
