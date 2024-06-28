package overcloud.blog.usecase.blog.get_article_list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetArticlesResponse {
    @JsonProperty("articles")
    private List<GetArticlesSingleResponse> articles;

    @JsonProperty("articlesCount")
    private int articlesCount;

    public void addArticleCount() {
        this.articlesCount++;
    }
}
