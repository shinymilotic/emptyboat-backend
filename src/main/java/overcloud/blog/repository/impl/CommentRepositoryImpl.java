package overcloud.blog.repository.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.CommentEntity;
import overcloud.blog.repository.CommentRepository;
import overcloud.blog.repository.jparepository.JpaCommentRepository;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
    private final JpaCommentRepository jpa;

    public CommentRepositoryImpl(JpaCommentRepository jpa) {
        this.jpa = jpa;
    }
    @Override
    public void deleteByArticleId(UUID id) {
        jpa.deleteByArticleId(id);
    }
    @Override
    public void deleteById(UUID fromString) {
        jpa.deleteById(fromString);
    }
    @Override
    public CommentEntity save(CommentEntity commentEntity) {
        return jpa.save(commentEntity);
    }
    @Override
    public List<CommentEntity> findByArticleId(UUID articleId) {
        return jpa.findByArticleId(articleId);
    }
}
