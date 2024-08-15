package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "tags", schema = "public")
public class TagEntity {
    @Id
    private UUID tagId;

    @Column(name = "name")
    private String name;

    // @OneToMany(mappedBy = "tag")
    // private List<ArticleTag> articleTags;
}
