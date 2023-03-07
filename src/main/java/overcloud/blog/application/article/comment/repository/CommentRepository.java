package overcloud.blog.application.article.comment.repository;

import org.springframework.data.jpa.repository.Query;
import overcloud.blog.domain.article.comment.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {

    @Query("SELECT cm FROM CommentEntity cm WHERE cm.article.slug = :articleSlug")
    List<CommentEntity> findByArticleSlug(String articleSlug);
}
