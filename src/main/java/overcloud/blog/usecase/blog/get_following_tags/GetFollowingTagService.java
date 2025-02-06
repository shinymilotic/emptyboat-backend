package overcloud.blog.usecase.blog.get_following_tags;

import java.util.List;

public interface GetFollowingTagService {
    List<FollowingTagResponse> getFollowingTags();
}
