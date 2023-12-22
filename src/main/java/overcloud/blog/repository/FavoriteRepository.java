package overcloud.blog.repository.a;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.FavoriteId;
import overcloud.blog.entity.FavoriteEntity;

import java.util.List;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, FavoriteId> {

    @Query("""
            SELECT favorite FROM FavoriteEntity favorite\
             WHERE favorite.user.id = :userId AND favorite.article.id = :articleId \
            """)
    List<FavoriteEntity> findById(UUID userId, UUID articleId);

    @Modifying
    @Query("""
            DELETE FROM FavoriteEntity favorite \
             WHERE favorite.article.slug = :slug \
            """)
    void deleteByArticleSlug(String slug);
}
