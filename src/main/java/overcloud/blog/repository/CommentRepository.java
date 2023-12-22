package overcloud.blog.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import overcloud.blog.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {

    @Query("SELECT cm FROM CommentEntity cm WHERE cm.article.slug = :articleSlug")
    List<CommentEntity> findByArticleSlug(String articleSlug);

    @Modifying
    @Query("""
            DELETE FROM CommentEntity comment \
             WHERE comment.article.slug = :slug \
            """)
    void deleteByArticleSlug(String slug);
}
