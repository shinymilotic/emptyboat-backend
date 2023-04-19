package overcloud.blog.application.article.api.dto.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Collection;

@Builder
@JsonTypeName("articles")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class UpdateArticleRequest {
    @JsonProperty("id")
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Collection<String> getTagList() {
        return tagList;
    }

    public void setTagList(Collection<String> tagList) {
        this.tagList = tagList;
    }
}
