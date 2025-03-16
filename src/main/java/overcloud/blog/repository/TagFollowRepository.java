package overcloud.blog.repository;

import overcloud.blog.entity.TagFollowEntity;
import overcloud.blog.entity.TagFollowId;

import java.util.UUID;

public interface TagFollowRepository {
    TagFollowEntity save(TagFollowEntity tagFollowEntity);
    void delete(TagFollowId tagFollowId);
    void deleteByUserId(UUID userId);
    void deleteByTagId(UUID tagId);
}
