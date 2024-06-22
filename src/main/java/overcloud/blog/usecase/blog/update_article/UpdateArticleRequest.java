package overcloud.blog.usecase.blog.update_article;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import overcloud.blog.usecase.blog.common.ArticleResMsg;

@Builder
@JsonTypeName("article")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class UpdateArticleRequest {
    @JsonProperty("body")
    @NotBlank(message = ArticleResMsg.ARTICLE_BODY_NOTBLANK)
    private String body;
}
