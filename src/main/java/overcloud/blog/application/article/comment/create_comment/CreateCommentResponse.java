package overcloud.blog.application.article.comment.create_comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import overcloud.blog.application.article.core.AuthorResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonTypeName("comment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class CreateCommentResponse {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("createdAt")
    private String createdAt;

    @JsonProperty("body")
    private String body;

    @JsonProperty("author")
    private AuthorResponse author;
}
