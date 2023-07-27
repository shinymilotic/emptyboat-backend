package overcloud.blog.application.article.core.repository;

import overcloud.blog.application.article.core.ArticleEntity;

import java.util.List;

public interface SearchArticlesRepository {
    List<ArticleEntity> findByCriteria(String tag, String author, String favorited, int limit, int offset);

}
