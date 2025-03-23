package overcloud.blog.repository.impl;

import org.springframework.stereotype.Repository;
import overcloud.blog.repository.FollowRepository;
import overcloud.blog.repository.jparepository.JpaFollowRepository;
import overcloud.blog.entity.FollowEntity;

import java.util.UUID;

@Repository
public class FollowRepositoryImpl implements FollowRepository {
    private final JpaFollowRepository jpa;

    public FollowRepositoryImpl(JpaFollowRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void unfollow(String currentUsername, String followingUsername) {
        jpa.unfollow(currentUsername, followingUsername);
    }

    @Override
    public void save(FollowEntity followEntity) {
        jpa.save(followEntity);
    }

    @Override
    public void delete(FollowEntity followEntity) {
        jpa.delete(followEntity);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        jpa.deleteByUserId(userId);
    }
}
