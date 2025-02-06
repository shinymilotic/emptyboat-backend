package overcloud.blog.usecase.blog.get_following_tags;

import java.util.List;
import overcloud.blog.response.RestResponse;

public interface GetFollowingTagService {
    List<FollowingTagResponse> getFollowingTags();
}
