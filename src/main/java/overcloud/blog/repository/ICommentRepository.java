package overcloud.blog.repository;

import java.util.UUID;

public interface ICommentRepository {
    void deleteByArticleSlug(String slug);
    void deleteById(UUID fromString);
}
