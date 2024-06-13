package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.ArticleEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaArticleRepository extends JpaRepository<ArticleEntity, UUID> {
    @Query("SELECT ar FROM ArticleEntity ar WHERE ar.slug = :slug ")
    List<ArticleEntity> findBySlug(@Param("slug") String slug);

//    @Query("SELECT ar FROM ArticleEntity ar WHERE ar.title = :title ")
//    List<ArticleEntity> findByTitle(String title);

    @Modifying
    @Query(value = "UPDATE articles SET search_vector = to_tsvector('english', title || ' ' || body)", nativeQuery = true)
    void updateSearchVector();

    @Modifying
    @Query("")
    void deleteBySlug(String slug);

    @Query(value = "SELECT true  FROM articles a WHERE a.title = :title limit 1 ", nativeQuery = true)
    Optional<Boolean> isTitleExist(@Param("title") String title);
}
