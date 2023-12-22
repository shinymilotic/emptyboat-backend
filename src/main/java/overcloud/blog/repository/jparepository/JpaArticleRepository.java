package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import overcloud.blog.repository.SearchArticlesRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaArticleRepository extends JpaRepository<ArticleEntity, UUID> , JpaSpecificationExecutor<ArticleEntity>, SearchArticlesRepository {
    @Query("SELECT ar FROM ArticleEntity ar WHERE ar.slug = :slug ")
    List<ArticleEntity> findBySlug(@Param("slug") String slug);

    @Query("SELECT ar FROM ArticleEntity ar WHERE ar.title = :title ")
    List<ArticleEntity> findByTitle(String title);

    @Modifying
    @Query("")
    void deleteBySlug(String slug);
}
