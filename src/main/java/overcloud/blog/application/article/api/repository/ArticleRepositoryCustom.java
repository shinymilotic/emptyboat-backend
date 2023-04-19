package overcloud.blog.application.article.api.repository;

import overcloud.blog.domain.article.ArticleEntity;

import java.util.List;

public interface ArticleRepositoryCustom {
    List<ArticleEntity> findByCriteria(String tag, String author, String favorited, int limit, int offset, String searchParam);

}
