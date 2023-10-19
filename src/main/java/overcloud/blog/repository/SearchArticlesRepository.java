package overcloud.blog.repository;

import overcloud.blog.entity.ArticleEntity;

import java.util.List;

public interface SearchArticlesRepository {
    List<ArticleEntity> findByCriteria(String tag, String author, String favorited, int limit, int offset);

}
