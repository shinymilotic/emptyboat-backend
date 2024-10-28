package overcloud.blog.repository;

import overcloud.blog.entity.TagFollowEntity;

public interface ITagFollowRepository {
    TagFollowEntity save(TagFollowEntity tagFollowEntity);
}
