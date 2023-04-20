package overcloud.blog.domain.article.favorite;

import lombok.Getter;
import lombok.Setter;
import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.user.UserEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "favorites", schema = "public")
public class FavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
