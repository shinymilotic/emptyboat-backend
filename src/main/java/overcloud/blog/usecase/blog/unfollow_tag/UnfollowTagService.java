package overcloud.blog.usecase.blog.unfollow_tag;

import overcloud.blog.response.RestResponse;

public interface UnfollowTagService {
    RestResponse<Void> unfollowTag(String tagId);
}
