package overcloud.blog.application.tag.core;

import lombok.Getter;
import lombok.Setter;
import overcloud.blog.application.article_tag.core.ArticleTag;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "tags", schema = "public")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "tag")
    private List<ArticleTag> articleTags;
}
