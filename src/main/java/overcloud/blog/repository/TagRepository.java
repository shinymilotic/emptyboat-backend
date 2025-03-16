package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;
import jakarta.persistence.Tuple;
import overcloud.blog.entity.TagEntity;

public interface TagRepository {
    List<TagEntity> findByTagIds(List<String> tagList);
    void saveAll(List<TagEntity> tagForSave);
    List<TagEntity> findAll();
    List<Tuple> findAllWithFollowing(UUID userId);
    List<TagEntity> findFollowingTags(UUID userId);
    List<TagEntity> findTags(int pageNumber, int itemsPerPage);
    void deleteTag(UUID tagId);
}
