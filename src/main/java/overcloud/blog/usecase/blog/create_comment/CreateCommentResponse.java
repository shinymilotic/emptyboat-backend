package overcloud.blog.usecase.blog.create_comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import overcloud.blog.usecase.blog.common.AuthorResponse;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
