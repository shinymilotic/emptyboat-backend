package overcloud.blog.domain.article.favorite;

import lombok.Getter;
import lombok.Setter;
import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.article.FavoriteId;
import overcloud.blog.domain.user.UserEntity;
import jakarta.persistence.*;


@Entity
@Getter
@Setter
@Table(name = "favorites", schema = "public")
public class FavoriteEntity {
    @EmbeddedId
    private FavoriteId id;

    @ManyToOne
    @MapsId("articleId")
    private ArticleEntity article;

    @ManyToOne
    @MapsId("userId")
    private UserEntity user;
}
