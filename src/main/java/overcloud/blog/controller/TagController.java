package overcloud.blog.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import overcloud.blog.usecase.blog.common.ITagResponse;
import overcloud.blog.usecase.blog.create_tag.CreateTagRequest;
import overcloud.blog.usecase.blog.create_tag.CreateTagsService;
import overcloud.blog.usecase.blog.delete_tag.IDeleteTag;
import overcloud.blog.usecase.blog.follow_tag.FollowTagService;
import overcloud.blog.usecase.blog.get_following_tags.FollowingTagResponse;
import overcloud.blog.usecase.blog.get_following_tags.GetFollowingTagService;
import overcloud.blog.usecase.blog.get_tag_count.IGetTagCount;
import overcloud.blog.usecase.blog.get_tags.GetTagsService;
import overcloud.blog.usecase.blog.get_tags_admin.IGetTagAdmin;
import overcloud.blog.usecase.blog.get_tags_admin.TagResponse;
import overcloud.blog.usecase.blog.unfollow_tag.UnfollowTagService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TagController {
    private final CreateTagsService createTagService;
    private final GetTagsService getTagsService;
    private final FollowTagService followTagService;
    private final UnfollowTagService unfollowTagService;
    private final GetFollowingTagService getFollowingTagService;
    private final IGetTagAdmin getTagsAdmin;
    private final IGetTagCount getTagCount;
    private final IDeleteTag deleteTag;

    public TagController(CreateTagsService createTagService,
                         GetTagsService getTagsService,
                         FollowTagService followTagService,
                         UnfollowTagService unfollowTagService,
                         GetFollowingTagService getFollowingTagService,
                         IGetTagAdmin getTagsAdmin,
                         IGetTagCount getTagCount,
                         IDeleteTag deleteTag) {
        this.createTagService = createTagService;
        this.getTagsService = getTagsService;
        this.followTagService = followTagService;
        this.unfollowTagService = unfollowTagService;
        this.getFollowingTagService = getFollowingTagService;
        this.getTagsAdmin = getTagsAdmin;
        this.getTagCount = getTagCount;
        this.deleteTag = deleteTag;
    }

    @PostMapping(ApiConst.TAGS)
    public List<String> createTags(@RequestBody CreateTagRequest createTagRequest) {
        return createTagService.createTags(createTagRequest);
    }

    @GetMapping(ApiConst.TAGS)
    public List<ITagResponse> getTags(@RequestParam(required = false) Boolean following) {
        return getTagsService.getTags(following);
    }

    @GetMapping(ApiConst.FOLLOWING_TAGS)
    public List<FollowingTagResponse> getFollowedTags() {
        return getFollowingTagService.getFollowingTags();
    }

    @PostMapping(ApiConst.FOLLOW_TAG)
    public Void followTag(@PathVariable("id") String tagId) {
        return followTagService.followTag(tagId);
    }

    @PostMapping(ApiConst.UNFOLLOW_TAG)
    public Void unfollowTag(@PathVariable("id") String tagId) {
        return unfollowTagService.unfollowTag(tagId);
    }

    @GetMapping("/admin/tags")
    public List<TagResponse> getTagAdmin(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                          @RequestParam(value = "itemsPerPage", defaultValue = "10") int itemsPerPage) {
        return getTagsAdmin.getTagAdmin(pageNumber, itemsPerPage);
    }

    @GetMapping("/tagCount")
    public Long getTagCount() {
        return this.getTagCount.getTagCount();
    }

    @DeleteMapping("/tag")
    public Void deleteTag(@RequestParam(value = "tagId") String tagId) {
        return this.deleteTag.deleteTag(tagId);
    }
}
