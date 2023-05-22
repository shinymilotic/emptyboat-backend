package overcloud.blog.application.article.update_article;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Collection;

@Builder
@JsonTypeName("article")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class UpdateArticleRequest {

    @JsonProperty("title")
    @NotBlank(message = "article.create.not-blank")
    @Size(min = 1, max = 60, message ="article.create.size")
    private String title;

    @JsonProperty("description")
    @NotBlank(message = "article.description.not-blank")
    @Size(min = 1, max = 100, message = "article.description.size")
    private String description;

    @JsonProperty("body")
    @NotBlank(message = "article.body.not-blank")
    private String body;

    @JsonProperty("tagList")
    @NotEmpty(message = "article.tags.not-empty")
    private Collection<String> tagList;
}
