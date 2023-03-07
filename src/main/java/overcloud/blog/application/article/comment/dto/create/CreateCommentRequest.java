package overcloud.blog.application.article.comment.dto.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;


public class CreateCommentRequest {

    @JsonProperty("article-slug")
    @NotNull
    private String articleSlug;

    @JsonProperty("body")
    @NotNull
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getArticleSlug() {
        return articleSlug;
    }

    public void setArticleSlug(String articleSlug) {
        this.articleSlug = articleSlug;
    }
}
