package overcloud.blog.application.article.api.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.domain.article.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, UUID> , JpaSpecificationExecutor<ArticleEntity>, ArticleRepositoryCustom{

    @Query("SELECT ar FROM ArticleEntity ar WHERE ar.slug = :slug ")
    List<ArticleEntity> findBySlug(@Param("slug") String slug);



}
