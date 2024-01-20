package overcloud.blog.repository;

import overcloud.blog.usecase.blog.get_article_list.ArticleSummary;

import java.util.List;
import java.util.UUID;

public interface SearchArticlesRepository {
    List<ArticleSummary> findByCriteria(UUID currentUserId, String tag, String author, String favorited, int limit, String lastArticleId);

    List<ArticleSummary> search(String keyword, UUID currentUserId, int limit, String lastArticleId);

}
