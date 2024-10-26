package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.TagFollowEntity;
import overcloud.blog.entity.TagFollowId;

@Repository
public interface JpaTagFollowRepository extends JpaRepository<TagFollowEntity, TagFollowId> {

}
