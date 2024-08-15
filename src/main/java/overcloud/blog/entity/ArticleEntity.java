package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "articles", schema = "public")
public class ArticleEntity {
    @Id
    private UUID articleId;

    @Column(name = "author_id")
    private UUID authorId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "body")
    private String body;

//    @OneToMany(mappedBy = "article", orphanRemoval = true)
//    private List<ArticleTag> articleTags;

//    @OneToMany(mappedBy = "article", orphanRemoval = true)
//    private List<CommentEntity> comments;
//
//    @OneToMany(mappedBy = "article", orphanRemoval = true)
//    private List<FavoriteEntity> favorites;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArticleEntity that = (ArticleEntity) o;

        return articleId.equals(that.articleId);
    }

    @Override
    public int hashCode() {
        return articleId.hashCode();
    }

}
