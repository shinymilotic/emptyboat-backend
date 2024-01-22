package overcloud.blog.repository;

import overcloud.blog.usecase.blog.common.ArticleSummary;
import java.util.List;
import java.util.UUID;

public interface SearchArticlesRepository {
    List<ArticleSummary> findBy(UUID currentUserId, String tag, String author, String favorited, int limit, String lastArticleId);

    ArticleSummary findArticleBySlug(String slug, UUID currentUserId);

    List<ArticleSummary> search(String keyword, UUID currentUserId, int limit, String lastArticleId);

}
