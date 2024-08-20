package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Entity
@Getter
@Setter
@Table(name = "favorites", schema = "public")
public class FavoriteEntity implements Serializable {
    @EmbeddedId
    private FavoriteId id;

    // @ManyToOne
    // @MapsId("articleId")
    // private ArticleEntity article;

    // @ManyToOne
    // @MapsId("userId")
    // private UserEntity user;
}
