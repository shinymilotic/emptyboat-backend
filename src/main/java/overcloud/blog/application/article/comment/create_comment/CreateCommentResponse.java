package overcloud.blog.application.article.comment.create_comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CreateCommentResponse {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    @JsonProperty("body")
    private String body;

    @JsonProperty("author")
    private CreateCommentAuthorResponse author;
}
