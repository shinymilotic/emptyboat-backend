package overcloud.blog.application.article.update_article;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.application.article.core.AuthorResponse;

import java.time.LocalDateTime;

@JsonTypeName("articles")
@Getter
@Setter
@Builder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class UpdateArticleResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("body")
    private String body;

    @JsonProperty("tagList")
    private Iterable<String> tagList;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("favorited")
    private boolean favorited;

    @JsonProperty("favoritesCount")
    private int favoritesCount;

    @JsonProperty("author")
    private AuthorResponse author;
}
