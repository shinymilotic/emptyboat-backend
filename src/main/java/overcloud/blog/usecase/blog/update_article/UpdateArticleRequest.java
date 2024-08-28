package overcloud.blog.usecase.blog.update_article;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import overcloud.blog.usecase.blog.common.ArticleResMsg;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArticleRequest {
    @JsonProperty("title")
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
