package overcloud.blog.application.article.api.dto.delete;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteArticleResponse {
    @JsonProperty("id")
    private String id;
}
