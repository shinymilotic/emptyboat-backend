package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;
import overcloud.blog.entity.FavoriteEntity;
import overcloud.blog.entity.FavoriteId;

public interface FavoriteRepository {
    List<FavoriteEntity> findById(UUID userId, UUID articleId);
    void deleteByArticleId(UUID id);
    void deleteById(FavoriteId favoritePk);
    void save(FavoriteEntity favoriteEntity);
    void deleteByUserId(UUID userId);
}
