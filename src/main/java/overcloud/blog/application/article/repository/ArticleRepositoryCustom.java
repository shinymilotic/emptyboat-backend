package overcloud.blog.application.article.repository;

import overcloud.blog.application.article.ArticleEntity;

import java.util.List;

public interface ArticleRepositoryCustom {
    List<ArticleEntity> findByCriteria(String tag, String author, String favorited, int limit, int offset, String searchParam);

}
