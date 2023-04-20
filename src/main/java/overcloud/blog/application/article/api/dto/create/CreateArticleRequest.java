package overcloud.blog.application.article.api.dto.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
    @NotNull
    private String title;

    @JsonProperty("description")
    @NotNull
    @Size(min = 1, max = 60, message = "Description size must between 1 and 60")
    private String description;

    @JsonProperty("body")
    @NotNull
    private String body;

    @JsonProperty("tagList")
    private Collection<String> tagList;
}
