package overcloud.blog.usecase.blog.get_following_tags;

import java.util.List;
import overcloud.blog.response.RestResponse;

public interface GetFollowingTagService {
    RestResponse<List<FollowingTagResponse>> getFollowingTags();
}
