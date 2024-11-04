package overcloud.blog.usecase.blog.get_following_tags;

import java.util.List;
import overcloud.blog.response.RestResponse;
import overcloud.blog.usecase.blog.common.ITagResponse;

public interface GetFollowingTagService {
    RestResponse<List<ITagResponse>> getFollowingTags();
}
