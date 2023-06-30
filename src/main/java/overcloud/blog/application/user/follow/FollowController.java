package overcloud.blog.application.user.follow;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import overcloud.blog.application.user.follow.make_follow.FollowResponse;
import overcloud.blog.application.user.follow.make_follow.FollowService;
import overcloud.blog.application.user.follow.make_unfollow.UnfollowResponse;
import overcloud.blog.application.user.follow.make_unfollow.UnfollowService;
import overcloud.blog.infrastructure.ApiConst;

@RestController
public class FollowController {

    private final FollowService followService;

    private final UnfollowService unfollowService;

    public FollowController(FollowService followService, UnfollowService unfollowService) {
        this.followService = followService;
        this.unfollowService = unfollowService;
    }

    @PostMapping(ApiConst.PROFILES_USERNAME_FOLLOW)
    public FollowResponse followUser(@PathVariable("username") String username) {
        return followService.followUser(username);
    }

    @DeleteMapping(ApiConst.PROFILES_USERNAME_FOLLOW)
    public UnfollowResponse unfollowUser(@PathVariable("username") String username) {
        return unfollowService.unfollowUser(username);
    }
}
