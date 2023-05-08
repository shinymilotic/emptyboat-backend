package overcloud.blog.application.article.get_article;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@JsonTypeName("article")
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class GetArticleResponse {
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
    private List<String> tagList;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    @JsonProperty("favorited")
    private boolean favorited;

    @JsonProperty("favoritesCount")
    private int favoritesCount;

    @JsonProperty("author")
    private GetArticlesAuthorResponse author;
}
