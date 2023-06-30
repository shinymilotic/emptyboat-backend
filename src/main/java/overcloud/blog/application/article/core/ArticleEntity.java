package overcloud.blog.application.article.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.application.article_tag.core.ArticleTag;
import overcloud.blog.application.article.comment.core.CommentEntity;
import overcloud.blog.application.article.favorite.core.FavoriteEntity;
import overcloud.blog.application.tag.core.TagEntity;
import overcloud.blog.application.user.core.UserEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name = "articles", schema = "public")
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @Column(name = "slug")
    private String slug;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "body")
    private String body;

    @OneToMany(mappedBy = "article")
    private List<ArticleTag> articleTags;

    @OneToMany(mappedBy = "article")
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "article")
    private List<FavoriteEntity> favorites;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ArticleEntity() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArticleEntity that = (ArticleEntity) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public List<String> getTagNameList() {
        List<String> list = new ArrayList<>();

        if (articleTags != null && articleTags.size() > 0) {
            list = articleTags.stream()
                    .map(ArticleTag::getTag)
                    .map(TagEntity::getName).toList();
        }

        return list;
    }
}
