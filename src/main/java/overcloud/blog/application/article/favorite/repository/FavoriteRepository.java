package overcloud.blog.application.article.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.domain.article.FavoriteId;
import overcloud.blog.domain.article.favorite.FavoriteEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, FavoriteId> {

    @Modifying
    @Query("DELETE FROM FavoriteEntity favorite" +
            "  WHERE EXISTS (SELECT article FROM ArticleEntity article" +
            " WHERE article.slug = :slug AND favorite.article.id = article.id) ")
    void deleteBySlug(String slug);

    @Modifying
    @Query("DELETE FROM FavoriteEntity favorite" +
            " WHERE favorite.article.id = :uuid ")
    void deleteByArticle(UUID uuid);

    @Query("SELECT favorite FROM FavoriteEntity favorite" +
            " WHERE favorite.user.id = :userId AND favorite.article.id = :articleId ")
    List<FavoriteEntity> findById(UUID userId, UUID articleId);
}
