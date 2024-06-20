package overcloud.blog.usecase.user.get_followers;

import java.util.UUID;

import overcloud.blog.usecase.common.response.RestResponse;

public interface GetFollowers {
    RestResponse<FollowerListResposne> getFollowers(UUID userId);
}
