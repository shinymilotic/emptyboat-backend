package overcloud.blog.application.article.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ModelAttribute;
import overcloud.blog.domain.article.favorite.FavoriteEntity;

import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, UUID> {

    @Modifying
    @Query("DELETE FROM FavoriteEntity favorite" +
            "  WHERE EXISTS (SELECT article FROM ArticleEntity article" +
            " WHERE article.slug = :slug AND favorite.article.id = article.id) ")
    void deleteBySlug(String slug);

    @Modifying
    @Query("DELETE FROM FavoriteEntity favorite" +
            " WHERE favorite.article.id = :uuid ")
    void deleteByArticle(UUID uuid);
}
