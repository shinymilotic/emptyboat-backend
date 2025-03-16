package overcloud.blog.repository;

import java.util.List;
import java.util.UUID;

import overcloud.blog.entity.ArticleTag;

public interface ArticleTagRepository {
    void saveAll(List<ArticleTag> articleTags);
    void deleteByArticleId(UUID articleId);
    void deleteByTagId(UUID uuidTagId);
}
