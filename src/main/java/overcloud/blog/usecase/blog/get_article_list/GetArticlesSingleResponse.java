package overcloud.blog.usecase.blog.get_article_list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class GetArticlesSingleResponse {
    @JsonProperty("id")
    private String id;

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

    @JsonProperty("favorited")
    private Boolean favorited;

    @JsonProperty("favoritesCount")
    private Long favoritesCount;

    @JsonProperty("author")
    private AuthorResponse author;
}
