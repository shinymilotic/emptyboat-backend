package overcloud.blog.application.article.create_article;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.Collection;

@Builder
@JsonTypeName("articles")
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class CreateArticleRequest {

    @JsonProperty("title")
    @NotBlank(message = "Title must be specified")
    @Size(min = 1, max = 60, message ="Title length must be between 1 and 60 characters")
    private String title;

    @JsonProperty("description")
    @NotBlank(message = "Description must be specified")
    @Size(min = 1, max = 100, message = "Description size must between 1 and 100")
    private String description;

    @JsonProperty("body")
    @NotBlank(message = "Article body must be specified")
    private String body;

    @JsonProperty("tagList")
    @NotEmpty(message = "Tag must be specified")
    private Collection<String> tagList;
}
