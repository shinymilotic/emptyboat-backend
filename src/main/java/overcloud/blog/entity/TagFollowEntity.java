package overcloud.blog.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "tag_follows", schema = "public")
public class TagFollowEntity {
    @EmbeddedId
    private TagFollowId tagFollowId;
}
