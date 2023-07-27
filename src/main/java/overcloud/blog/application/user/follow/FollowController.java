package overcloud.blog.application.user.follow;

import org.springframework.web.bind.annotation.*;
import overcloud.blog.application.user.follow.make_follow.FollowResponse;
import overcloud.blog.application.user.follow.make_follow.FollowService;
import overcloud.blog.application.user.follow.make_unfollow.UnfollowResponse;
import overcloud.blog.application.user.follow.make_unfollow.UnfollowService;
import overcloud.blog.infrastructure.ApiConst;

@CrossOrigin(origins = "*", maxAge = 3600)
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
