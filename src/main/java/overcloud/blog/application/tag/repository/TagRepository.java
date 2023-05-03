package overcloud.blog.application.tag.repository;

import org.springframework.data.jpa.repository.Query;
import overcloud.blog.application.tag.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, UUID> {

    @Query("SELECT tag FROM TagEntity tag WHERE tag.name = :tag")
    TagEntity findByTagName(String tag);

    @Query("SELECT tag FROM TagEntity tag WHERE tag.name IN (:tagList)")
    List<TagEntity> findByTagName(Iterable<String> tagList);

    @Query("SELECT tag FROM TagEntity tag WHERE tag.name IN (:tagList) ")
    List<TagEntity> checkAllTagsExistDB(Collection<String> tagList);
}
