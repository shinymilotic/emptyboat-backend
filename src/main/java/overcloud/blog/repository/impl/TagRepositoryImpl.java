package overcloud.blog.repository.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.repository.ITagRepository;
import overcloud.blog.repository.jparepository.JpaTagRepository;

@Repository
public class TagRepositoryImpl implements ITagRepository {

    private final JpaTagRepository jpa;

    public TagRepositoryImpl(JpaTagRepository jpa) {
        this.jpa = jpa;
    }
    @Override
    public List<TagEntity> findByTagName(List<String> tagList) {
        return jpa.findByTagName(tagList);
    }
    @Override
    public void saveAll(List<TagEntity> tagForSave) {
        jpa.saveAll(tagForSave);
    }
    @Override
    public List<TagEntity> findAll() {
        return jpa.findAll();
    }
}
