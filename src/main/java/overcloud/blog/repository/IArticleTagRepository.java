package overcloud.blog.repository;

import java.util.List;
import overcloud.blog.entity.ArticleTag;

public interface IArticleTagRepository {
    void saveAll(List<ArticleTag> articleTags);
}
