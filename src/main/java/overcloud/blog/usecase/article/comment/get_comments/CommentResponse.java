package overcloud.blog.usecase.article.comment.get_comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.article.comment.core.AuthorResposne;

import java.util.UUID;

@Builder
@Getter
@Setter
public class CommentResponse {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("createdAt")
    private String createdAt;

    @JsonProperty("body")
    private String body;

    @JsonProperty("author")
    private AuthorResposne authorResponse;
}
