package overcloud.blog.application.article.api.dto.get.multiple;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetArticlesResponse {

    @JsonProperty("articles")
    private List<GetArticlesSingleResponse> articles;

    public List<GetArticlesSingleResponse> getArticles() {
        return articles;
    }

    public void setArticles(List<GetArticlesSingleResponse> articles) {
        this.articles = articles;
    }
}
