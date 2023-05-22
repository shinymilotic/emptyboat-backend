package overcloud.blog.application.article.comment.get_comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.application.article.comment.core.AuthorResposne;

import java.util.UUID;

@Builder
@Getter
@Setter
public class CommentResponse {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("body")
    private String body;

    @JsonProperty("author")
    private AuthorResposne authorResponse;
}
