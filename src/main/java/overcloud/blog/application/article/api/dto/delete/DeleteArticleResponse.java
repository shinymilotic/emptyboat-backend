package overcloud.blog.application.article.api.dto.delete;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteArticleResponse {

    @JsonProperty("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
