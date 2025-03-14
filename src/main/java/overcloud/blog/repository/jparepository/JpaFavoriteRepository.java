package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.FavoriteEntity;
import overcloud.blog.entity.FavoriteId;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaFavoriteRepository extends JpaRepository<FavoriteEntity, FavoriteId> {

    @Query("""
            SELECT favorite FROM FavoriteEntity favorite\
             WHERE favorite.id.userId = :userId AND favorite.id.articleId = :articleId \
            """)
    List<FavoriteEntity> findById(UUID userId, UUID articleId);

    @Modifying
    @Query("""
            DELETE FROM FavoriteEntity favorite \
             WHERE favorite.id.articleId = :id \
            """)
    void deleteByArticleId(UUID id);

    @Modifying
    @Query("DELETE FROM FavoriteEntity f WHERE f.id.userId = :userId")
    void deleteByUserId(UUID userId);
}
