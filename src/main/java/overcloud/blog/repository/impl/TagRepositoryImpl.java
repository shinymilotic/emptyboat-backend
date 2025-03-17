package overcloud.blog.repository.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import jakarta.persistence.Tuple;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.repository.TagRepository;
import overcloud.blog.repository.jparepository.JpaTagRepository;

@Repository
public class TagRepositoryImpl implements TagRepository {
    private final JpaTagRepository jpa;

    public TagRepositoryImpl(JpaTagRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<TagEntity> findByTagIds(List<String> tagList) {
        return jpa.findByTagIds(tagList);
    }

    @Override
    public Optional<TagEntity> findByTagId(UUID tagId) {
        return jpa.findById(tagId);
    }

    @Override
    public void saveAll(List<TagEntity> tagForSave) {
        jpa.saveAll(tagForSave);
    }
    
    @Override
    public List<TagEntity> findAll() {
        return jpa.findAll();
    }

    @Override
    public List<Tuple> findAllWithFollowing(UUID userId) {
        return jpa.findAllWithFollowing(userId);
    }

    @Override
    public List<TagEntity> findFollowingTags(UUID userId) {
        return this.jpa.findFollowingTags(userId);
    }

    @Override
    public List<TagEntity> findTags(int pageNumber, int itemsPerPage) {
        int offset = pageNumber - 1;
        return jpa.findTags(itemsPerPage, offset);
    }

    @Override
    public void deleteTag(UUID tagId) {
        jpa.deleteById(tagId);
    }

    @Override
    public void save(TagEntity tag) {
        jpa.save(tag);
    }
}
