package overcloud.blog.controller.admin;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import overcloud.blog.usecase.admin.tag.create_tag.CreateTagRequest;
import overcloud.blog.usecase.admin.tag.create_tag.CreateTagService;
import overcloud.blog.usecase.admin.tag.delete_tag.DeleteTag;
import overcloud.blog.usecase.admin.tag.get_tags.GetTagList;
import overcloud.blog.usecase.admin.tag.get_tags.TagListResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AdminTagController {
    private final CreateTagService createTagService;
    private final GetTagList getTagsAdmin;
    private final DeleteTag deleteTag;

    public AdminTagController(CreateTagService createTagService,
                              GetTagList getTagsAdmin,
                              DeleteTag deleteTag) {
        this.createTagService = createTagService;
        this.getTagsAdmin = getTagsAdmin;
        this.deleteTag = deleteTag;
    }

    @PostMapping("/admin/tags")
    public List<String> createTags(@RequestBody CreateTagRequest createTagRequest) {
        return createTagService.createTags(createTagRequest);
    }

    @GetMapping("/admin/tags")
    public TagListResponse getTagList(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                      @RequestParam(value = "itemsPerPage", defaultValue = "10") int itemsPerPage) {
        return getTagsAdmin.getTagList(pageNumber, itemsPerPage);
    }

    @DeleteMapping("/admin/tags")
    public Void deleteTag(@RequestParam(value = "tagId") String tagId) {
        return this.deleteTag.deleteTag(tagId);
    }
}
