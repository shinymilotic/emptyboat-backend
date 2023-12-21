package overcloud.blog.usecase.article.comment.get_comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCommentRequest {
    @JsonProperty("article-slug")
    private String articleSLug;
}
