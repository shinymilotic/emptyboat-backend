package overcloud.blog.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import overcloud.blog.usecase.blog.common.TagResponse;
import overcloud.blog.usecase.blog.follow_tag.FollowTagService;
import overcloud.blog.usecase.blog.get_following_tags.FollowingTagResponse;
import overcloud.blog.usecase.blog.get_following_tags.GetFollowingTagService;
import overcloud.blog.usecase.blog.get_tags.GetTagsService;
import overcloud.blog.usecase.blog.unfollow_tag.UnfollowTagService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TagController {
    private final GetTagsService getTagsService;
    private final FollowTagService followTagService;
    private final UnfollowTagService unfollowTagService;
    private final GetFollowingTagService getFollowingTagService;

    public TagController(GetTagsService getTagsService,
                         FollowTagService followTagService,
                         UnfollowTagService unfollowTagService,
                         GetFollowingTagService getFollowingTagService) {
        this.getTagsService = getTagsService;
        this.followTagService = followTagService;
        this.unfollowTagService = unfollowTagService;
        this.getFollowingTagService = getFollowingTagService;
    }

    @GetMapping("/tags")
    public List<TagResponse> getTags(@RequestParam(required = false) Boolean following) {
        return getTagsService.getTags(following);
    }

    @GetMapping("/followingTags")
    public List<FollowingTagResponse> getFollowedTags() {
        return getFollowingTagService.getFollowingTags();
    }

    @PostMapping("/tags/{id}/follow")
    public Void followTag(@PathVariable("id") String tagId) {
        return followTagService.followTag(tagId);
    }

    @PostMapping("/tags/{id}/unfollow")
    public Void unfollowTag(@PathVariable("id") String tagId) {
        return unfollowTagService.unfollowTag(tagId);
    }
}
