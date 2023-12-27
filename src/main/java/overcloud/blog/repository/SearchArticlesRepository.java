package overcloud.blog.repository;

import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.usecase.article.get_article_list.ArticleSummary;

import java.util.List;

public interface SearchArticlesRepository {
    List<ArticleSummary> findByCriteria(String tag, String author, String favorited, int limit, int offset);

}
