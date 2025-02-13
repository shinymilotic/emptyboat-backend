package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.ArticleEntity;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaArticleRepository extends JpaRepository<ArticleEntity, UUID> {
//    @Query("SELECT ar FROM ArticleEntity ar WHERE ar.title = :title ")
//    List<ArticleEntity> findByTitle(String title);

    @Modifying
    @Query(value = "UPDATE articles SET search_vector = to_tsvector('english', title || ' ' || body)", nativeQuery = true)
    void updateSearchVector();

    @Modifying
    @Query("DELETE FROM ArticleEntity ar WHERE ar.articleId = :articleId ")
    void deleteById(@Param("articleId") UUID articleId);

    @Query(value = "SELECT true  FROM articles a WHERE a.title = :title limit 1 ", nativeQuery = true)
    Optional<Boolean> isTitleExist(@Param("title") String title);

    @Modifying
    @Query("DELETE FROM ArticleEntity ar WHERE ar.authorId = :userId")
    void deleteByUserId(@Param("userId") UUID userId);
}
