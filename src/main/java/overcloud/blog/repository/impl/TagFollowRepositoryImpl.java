package overcloud.blog.repository.impl;

import org.springframework.stereotype.Repository;
import overcloud.blog.entity.TagFollowEntity;
import overcloud.blog.entity.TagFollowId;
import overcloud.blog.repository.ITagFollowRepository;
import overcloud.blog.repository.jparepository.JpaTagFollowRepository;

import java.util.UUID;

@Repository
public class TagFollowRepositoryImpl implements ITagFollowRepository {
    private final JpaTagFollowRepository jpa;

    public TagFollowRepositoryImpl(JpaTagFollowRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public TagFollowEntity save(TagFollowEntity tagFollowEntity) {
        return this.jpa.save(tagFollowEntity);
    }

    @Override
    public void delete(TagFollowId tagFollowId) {
        jpa.deleteById(tagFollowId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        jpa.deleteByUserId(userId);
    }

}
