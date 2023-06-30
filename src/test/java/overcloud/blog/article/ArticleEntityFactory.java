package overcloud.blog.article;

import overcloud.blog.application.article.core.ArticleEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public class ArticleEntityFactory {
    public static ArticleEntity createArticleBySlug() {
        LocalDateTime now = LocalDateTime.now();
        return ArticleEntity.builder().id(UUID.randomUUID())
                .title("Contrasting Three Projects")
                .slug("contrasting-three-projects")
                .author(UserEntityFactory.createArticleTestNormal())
                .body("<p>The author describes three software development projects with different outc")
                .description("About three projects and DDD")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
