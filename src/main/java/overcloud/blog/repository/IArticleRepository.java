package overcloud.blog.repository;

import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.usecase.blog.common.ArticleSummary;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IArticleRepository {
    List<ArticleEntity> findBySlug(String slug);
    void updateSearchVector();
    void deleteBySlug(String slug);
    Optional<Boolean> isTitleExist(String title);
    List<ArticleSummary> findBy(UUID currentUserId, String tag, String author, String favorited, int limit, String lastArticleId);
    ArticleSummary findArticleBySlug(String slug, UUID currentUserId);
    List<ArticleSummary> search(String keyword, UUID currentUserId, int limit, String lastArticleId);
    List<UserEntity> findAllPaging(int page, int size);
    void save(ArticleEntity articleEntity);
}
