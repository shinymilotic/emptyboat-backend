package overcloud.blog.usecase.blog.get_article_list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthorResponse {

    @JsonProperty("username")
    private String username;

    @JsonProperty("bio")
    private String bio;

    @JsonProperty("image")
    private String image;

    @JsonProperty("following")
    private Boolean following;

    @JsonProperty("followersCount")
    private Long followersCount;
}
