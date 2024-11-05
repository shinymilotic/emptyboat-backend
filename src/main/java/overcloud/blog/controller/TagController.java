package overcloud.blog.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.response.RestResponse;
import overcloud.blog.usecase.blog.common.ITagResponse;
import overcloud.blog.usecase.blog.create_tag.CreateTagRequest;
import overcloud.blog.usecase.blog.create_tag.CreateTagsService;
import overcloud.blog.usecase.blog.follow_tag.FollowTagService;
import overcloud.blog.usecase.blog.get_following_tags.FollowingTagResponse;
import overcloud.blog.usecase.blog.get_following_tags.GetFollowingTagService;
import overcloud.blog.usecase.blog.get_tags.GetTagsService;
import overcloud.blog.usecase.blog.unfollow_tag.UnfollowTagService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TagController {
    private final CreateTagsService createTagService;
    private final GetTagsService getTagsService;
    private final FollowTagService followTagService;
    private final UnfollowTagService unfollowTagService;
    private final GetFollowingTagService getFollowingTagService;

    public TagController(CreateTagsService createTagService,
                         GetTagsService getTagsService,
                         FollowTagService followTagService,
                         UnfollowTagService unfollowTagService,
                         GetFollowingTagService getFollowingTagService) {
        this.createTagService = createTagService;
        this.getTagsService = getTagsService;
        this.followTagService = followTagService;
        this.unfollowTagService = unfollowTagService;
        this.getFollowingTagService = getFollowingTagService;
    }

    @PostMapping(ApiConst.TAGS)
    public RestResponse<List<String>> createTags(@RequestBody CreateTagRequest createTagRequest) {
        return createTagService.createTags(createTagRequest);
    }

    @GetMapping(ApiConst.TAGS)
    public RestResponse<List<ITagResponse>> getTags(@RequestParam(required = false) Boolean following) {
        return getTagsService.getTags(following);
    }

    @GetMapping(ApiConst.FOLLOWING_TAGS)
    public RestResponse<List<FollowingTagResponse>> getFollowedTags() {
        return getFollowingTagService.getFollowingTags();
    }
    

    @PostMapping(ApiConst.FOLLOW_TAG)
    public RestResponse<Void> followTag(@PathVariable("id") String tagId) {
        return followTagService.followTag(tagId);
    }

    @PostMapping(ApiConst.UNFOLLOW_TAG)
    public RestResponse<Void> unfollowTag(@PathVariable("id") String tagId) {
        return unfollowTagService.unfollowTag(tagId);
    }

}
