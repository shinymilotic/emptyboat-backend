package overcloud.blog.application.article.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.application.article_tag.ArticleTag;
import overcloud.blog.application.article_tag.ArticleTagId;

import java.util.UUID;

@Repository
public interface ArticleTagRepository extends JpaRepository<ArticleTag, ArticleTagId> {
    @Modifying
    @Query("DELETE FROM ArticleTag articleTag" +
            " WHERE articleTag.article.id = :articleId AND articleTag.tag.id = :tagId ")
    void deleteArticleTags(UUID articleId, UUID tagId);

}
