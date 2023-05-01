package overcloud.blog.domain.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.domain.article.tag.TagEntity;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "article_tag", schema = "public")
public class ArticleTag {
    @EmbeddedId
    private ArticleTagId id;

    @ManyToOne
    @MapsId("tagId")
    private TagEntity tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("articleId")
    private ArticleEntity article;

    public ArticleTag() {

    }
}
