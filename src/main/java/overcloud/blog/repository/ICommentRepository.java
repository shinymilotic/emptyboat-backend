package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;

import overcloud.blog.entity.CommentEntity;

public interface ICommentRepository {
    void deleteByArticleSlug(String slug);
    void deleteById(UUID fromString);
    CommentEntity save(CommentEntity commentEntity);
    List<CommentEntity> findByArticleSlug(String articleSlug);
}
