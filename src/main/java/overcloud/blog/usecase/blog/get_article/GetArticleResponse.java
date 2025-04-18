package overcloud.blog.usecase.blog.get_article;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.blog.common.TagResponseSimple;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class GetArticleResponse {
    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("body")
    private String body;
    @JsonProperty("tagList")
    private List<TagResponseSimple> tagList;
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    @JsonProperty("favorited")
    private Boolean favorited;
    @JsonProperty("favoritesCount")
    private Long favoritesCount;
    @JsonProperty("author")
    private AuthorResponse author;
}
