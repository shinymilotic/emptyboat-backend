package overcloud.blog.usecase.blog.delete_article;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteArticleResponse {
    @JsonProperty("slug")
    private String slug;
}
