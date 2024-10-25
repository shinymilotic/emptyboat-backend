package overcloud.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "tag_follows", schema = "public")
public class TagFollowEntity {
    @EmbeddedId
    private TagFollowId tagFollowId;
}
