package overcloud.blog.usecase.blog.update_article;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import overcloud.blog.usecase.blog.common.ArticleResMsg;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArticleRequest {
    @JsonProperty("body")
    @NotBlank(message = ArticleResMsg.ARTICLE_BODY_NOTBLANK)
    private String body;
}
