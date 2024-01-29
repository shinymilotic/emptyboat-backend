package overcloud.blog.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.io.Serializable;


@Entity
@Getter
@Setter
@Table(name = "favorites", schema = "public")
public class FavoriteEntity implements Serializable {
    @EmbeddedId
    private FavoriteId id;

    @ManyToOne
    @MapsId("articleId")
    private ArticleEntity article;

    @ManyToOne
    @MapsId("userId")
    private UserEntity user;
}
