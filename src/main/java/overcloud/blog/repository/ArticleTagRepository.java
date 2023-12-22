package overcloud.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import overcloud.blog.entity.ArticleTag;
import overcloud.blog.entity.ArticleTagId;

import java.util.UUID;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, ArticleTagId> {
    @Modifying
    @Query("""
            DELETE FROM ArticleTag articleTag\
             WHERE articleTag.article.id = :articleId AND articleTag.tag.id = :tagId \
            """)
    void deleteArticleTags(UUID articleId, UUID tagId);

}
