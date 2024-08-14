package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "article_tags", schema = "public")
public class ArticleTag {
    @EmbeddedId
    private ArticleTagId id;
}
