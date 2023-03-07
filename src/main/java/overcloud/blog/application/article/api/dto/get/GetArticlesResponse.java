package overcloud.blog.application.article.api.dto.get;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetArticlesResponse {

    @JsonProperty("articles")
    private List<ArticleResponse> articles;

    public List<ArticleResponse> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleResponse> articles) {
        this.articles = articles;
    }
}
