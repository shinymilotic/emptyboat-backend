package overcloud.blog.controller;

import org.springframework.web.bind.annotation.*;

import overcloud.blog.response.RestResponse;
import overcloud.blog.usecase.user.follow.make_follow.FollowService;
import overcloud.blog.usecase.user.follow.make_unfollow.UnfollowService;

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
    public RestResponse<Void> followUser(@PathVariable String username) {
        return followService.followUser(username);
    }

    @DeleteMapping(ApiConst.PROFILES_USERNAME_FOLLOW)
    public RestResponse<Void> unfollowUser(@PathVariable String username) {
        return unfollowService.unfollowUser(username);
    }
}
