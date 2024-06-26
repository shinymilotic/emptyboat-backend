package overcloud.blog.repository.impl;

import java.util.UUID;

import org.springframework.stereotype.Repository;
import overcloud.blog.repository.ICommentRepository;
import overcloud.blog.repository.jparepository.JpaCommentRepository;

@Repository
public class CommentRepositoryImpl implements ICommentRepository {
    private final JpaCommentRepository jpa;

    public CommentRepositoryImpl(JpaCommentRepository jpa) {
        this.jpa = jpa;
    }
    @Override
    public void deleteByArticleSlug(String slug) {
        jpa.deleteByArticleSlug(slug);
    }
    @Override
    public void deleteById(UUID fromString) {
        jpa.deleteById(fromString);
    }
}
