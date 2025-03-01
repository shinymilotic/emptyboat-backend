package overcloud.blog.repository;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.usecase.blog.common.ArticleSummary;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IArticleRepository {
    Optional<ArticleEntity> findById(UUID id);
    void updateSearchVector();
    void deleteById(UUID id);
    Optional<Boolean> isTitleExist(String title);
    List<ArticleSummary> findBy(UUID currentUserId, UUID tagId, String author, String favorited, int limit, String lastArticleId);
    ArticleSummary findArticleById(UUID id, UUID currentUserId);
    List<ArticleSummary> search(String keyword, UUID currentUserId, int limit, String lastArticleId);
    void save(ArticleEntity articleEntity);
    void deleteByUserId(UUID userId);
}
