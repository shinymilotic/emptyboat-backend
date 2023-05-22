package overcloud.blog.application.article.comment.create_comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.application.article.core.AuthorResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
public class CreateCommentResponse {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("createdAt")
    private String createdAt;

    @JsonProperty("updatedAt")
    private String updatedAt;

    @JsonProperty("body")
    private String body;

    @JsonProperty("author")
    private AuthorResponse author;
}
