package overcloud.blog.domain.article.favorite;

import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.user.UserEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "favorites", schema = "public")
public class FavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public ArticleEntity getArticle() {
        return article;
    }

    public void setArticle(ArticleEntity article) {
        this.article = article;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
