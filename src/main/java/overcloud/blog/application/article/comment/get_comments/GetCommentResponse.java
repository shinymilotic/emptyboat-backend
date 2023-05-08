package overcloud.blog.application.article.comment.get_comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class GetCommentResponse {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("body")
    private String body;

    @JsonProperty("author")
    private GetCommentAuthorResponse authorResponse;
}
