package overcloud.blog.usecase.article.get_article_list;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSummary {
    private String id;

    private String slug;

    private String title;

    private String description;

    private String body;

    private String tag;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean favorited;

    private int favoritesCount;

    private String username;

    private String bio;

    private String image;

    private boolean following;

    private int followersCount;
}
