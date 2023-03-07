package overcloud.blog.application.article.comment.dto.get;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetCommentRequest {

    @JsonProperty("article-slug")
    private String articleSLug;

    public String getArticleSLug() {
        return articleSLug;
    }

    public void setArticleSLug(String articleSLug) {
        this.articleSLug = articleSLug;
    }
}
