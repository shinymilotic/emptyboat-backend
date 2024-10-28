package overcloud.blog.repository.impl;

import org.springframework.stereotype.Repository;
import overcloud.blog.entity.TagFollowEntity;
import overcloud.blog.repository.ITagFollowRepository;
import overcloud.blog.repository.jparepository.JpaTagFollowRepository;

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
    
}
