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
import overcloud.blog.usecase.blog.get_tags.GetTagsService;
import overcloud.blog.usecase.blog.get_tags_admin.IGetTagAdmin;
import overcloud.blog.usecase.blog.get_tags_admin.TagResponse;
import overcloud.blog.usecase.blog.unfollow_tag.UnfollowTagService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AdminTagController {
    private final CreateTagsService createTagService;
    private final IGetTagAdmin getTagsAdmin;
    private final IDeleteTag deleteTag;

    public AdminTagController(CreateTagsService createTagService,
                         IGetTagAdmin getTagsAdmin,
                         IDeleteTag deleteTag) {
        this.createTagService = createTagService;
        this.getTagsAdmin = getTagsAdmin;
        this.deleteTag = deleteTag;
    }

    @PostMapping("/admin/tags")
    public List<String> createTags(@RequestBody CreateTagRequest createTagRequest) {
        return createTagService.createTags(createTagRequest);
    }

    @GetMapping("/admin/tags")
    public List<TagResponse> getTagAdmin(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                         @RequestParam(value = "itemsPerPage", defaultValue = "10") int itemsPerPage) {
        return getTagsAdmin.getTagAdmin(pageNumber, itemsPerPage);
    }

    @DeleteMapping("/admin/tags")
    public Void deleteTag(@RequestParam(value = "tagId") String tagId) {
        return this.deleteTag.deleteTag(tagId);
    }
}
