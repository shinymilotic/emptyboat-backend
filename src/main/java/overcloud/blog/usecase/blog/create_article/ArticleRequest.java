package overcloud.blog.usecase.blog.create_article;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.blog.common.ArticleResMsg;

import java.util.List;

@Builder
@JsonTypeName("article")
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class ArticleRequest {
    @JsonProperty("title")
    @NotBlank(message = ArticleResMsg.ARTICLE_TITLE_NOTBLANK)
    @Size(min = 1, max = 60, message = ArticleResMsg.ARTICLE_TITLE_SIZE)
    private String title;

    @JsonProperty("description")
    @NotBlank(message = ArticleResMsg.ARTICLE_DESCRIPTION_NOTBLANK)
    @Size(min = 1, max = 100, message = ArticleResMsg.ARTICLE_DESCRIPTION_SIZE)
    private String description;

    @JsonProperty("body")
    @NotBlank(message = ArticleResMsg.ARTICLE_BODY_NOTBLANK)
    private String body;

    @JsonProperty("tagList")
    @NotEmpty(message = ArticleResMsg.ARTICLE_TAGS_NOTEMPTY)
    private List<String> tagList;
}
