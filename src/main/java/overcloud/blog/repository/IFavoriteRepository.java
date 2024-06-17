package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import overcloud.blog.entity.FavoriteEntity;

public interface IFavoriteRepository {
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
