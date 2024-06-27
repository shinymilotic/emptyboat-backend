package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;
import overcloud.blog.entity.FavoriteEntity;
import overcloud.blog.entity.FavoriteId;

public interface IFavoriteRepository {
    List<FavoriteEntity> findById(UUID userId, UUID articleId);
    void deleteByArticleSlug(String slug);
    void deleteById(FavoriteId favoritePk);
    void save(FavoriteEntity favoriteEntity);
}
