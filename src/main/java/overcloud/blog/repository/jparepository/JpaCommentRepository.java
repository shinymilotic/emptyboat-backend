package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.CommentEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaCommentRepository extends JpaRepository<CommentEntity, UUID> {

    @Query("SELECT cm FROM CommentEntity cm WHERE cm.article.articleId = :articleId")
    List<CommentEntity> findByArticleId(UUID articleId);

    @Modifying
    @Query("""
            DELETE FROM CommentEntity comment \
             WHERE comment.article.articleId = :id \
            """)
    void deleteByArticleId(UUID id);
}
