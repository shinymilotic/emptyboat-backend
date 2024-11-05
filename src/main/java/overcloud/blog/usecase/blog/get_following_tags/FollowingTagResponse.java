package overcloud.blog.usecase.blog.get_following_tags;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FollowingTagResponse {
    private String id;
    private String name;
}
