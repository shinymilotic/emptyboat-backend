package overcloud.blog.application.article.comment.dto.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCommentRequest {
    @JsonProperty("article-slug")
    private String articleSLug;
}
