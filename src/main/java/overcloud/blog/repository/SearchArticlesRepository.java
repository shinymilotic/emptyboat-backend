package overcloud.blog.repository;

import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.usecase.article.get_article_list.ArticleSummary;

import java.util.List;
import java.util.UUID;

public interface SearchArticlesRepository {
    List<ArticleSummary> findByCriteria(UUID currentUserId, String tag, String author, String favorited, int limit, int offset);

}
