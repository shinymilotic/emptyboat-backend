package overcloud.blog.domain.article;

import lombok.Getter;
import lombok.Setter;
import overcloud.blog.domain.article.tag.TagEntity;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "article_tag", schema = "public")
public class ArticleTag {

    @EmbeddedId
    private ArticleTagId id;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    @MapsId("tagId")
    private TagEntity tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @MapsId("articleId")
    private ArticleEntity article;

    public ArticleTag() {
    }
}
