package overcloud.blog.application.article.search.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleBinLogValue {
    @JsonProperty("before")
    private ArticleSyncData before;

    @JsonProperty("after")
    private ArticleSyncData after;

    @JsonProperty("op")
    private String op;
}
