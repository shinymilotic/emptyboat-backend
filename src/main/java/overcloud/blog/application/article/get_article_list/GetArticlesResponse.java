package overcloud.blog.application.article.get_article_list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetArticlesResponse {
    @JsonProperty("articles")
    private List<GetArticlesSingleResponse> articles;
}
