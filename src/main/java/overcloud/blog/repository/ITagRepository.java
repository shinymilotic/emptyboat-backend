package overcloud.blog.repository;

import java.util.List;

import overcloud.blog.entity.TagEntity;

public interface ITagRepository {
    List<TagEntity> findByTagName(List<String> tagList);
    void saveAll(List<TagEntity> tagForSave);
}
