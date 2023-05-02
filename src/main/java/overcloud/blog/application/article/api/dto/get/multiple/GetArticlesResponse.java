package overcloud.blog.application.article.api.dto.get.multiple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetArticlesResponse {
    @JsonProperty("articles")
    private List<GetArticlesSingleResponse> articles;
}
