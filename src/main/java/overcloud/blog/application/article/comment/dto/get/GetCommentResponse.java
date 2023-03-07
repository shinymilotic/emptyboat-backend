package overcloud.blog.application.article.comment.dto.get;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public GetCommentAuthorResponse getAuthorResponse() {
        return authorResponse;
    }

    public void setAuthorResponse(GetCommentAuthorResponse authorResponse) {
        this.authorResponse = authorResponse;
    }
}
