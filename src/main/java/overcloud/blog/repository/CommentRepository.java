package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;

import overcloud.blog.entity.CommentEntity;

public interface CommentRepository {
    void deleteByArticleId(UUID id);
    void deleteById(UUID id);
    CommentEntity save(CommentEntity commentEntity);
    List<CommentEntity> findByArticleId(UUID articleId);
}
