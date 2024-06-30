package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.ArticleTag;
import overcloud.blog.entity.ArticleTagId;
import java.util.UUID;

@Repository
public interface JpaArticleTagRepository extends JpaRepository<ArticleTag, ArticleTagId> {
    @Modifying
    @Query("""
            DELETE FROM ArticleTag articleTag\
             WHERE articleTag.id.articleId = :articleId AND articleTag.id.tagId = :tagId \
            """)
    void deleteArticleTags(UUID articleId, UUID tagId);

}
