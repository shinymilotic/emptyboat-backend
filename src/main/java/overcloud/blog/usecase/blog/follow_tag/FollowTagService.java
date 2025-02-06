package overcloud.blog.usecase.blog.follow_tag;

import overcloud.blog.response.RestResponse;

public interface FollowTagService {
        Void followTag(String tagId);
}
