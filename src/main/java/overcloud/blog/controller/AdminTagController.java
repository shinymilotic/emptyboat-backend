package overcloud.blog.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import overcloud.blog.usecase.blog.create_tag.CreateTagRequest;
import overcloud.blog.usecase.blog.create_tag.CreateTagService;
import overcloud.blog.usecase.blog.create_tag.CreateTagServiceImpl;
import overcloud.blog.usecase.blog.delete_tag.DeleteTag;
import overcloud.blog.usecase.blog.get_tags_admin.GetTagAdmin;
import overcloud.blog.usecase.blog.get_tags_admin.TagResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AdminTagController {
    private final CreateTagService createTagService;
    private final GetTagAdmin getTagsAdmin;
    private final DeleteTag deleteTag;

    public AdminTagController(CreateTagService createTagService,
                              GetTagAdmin getTagsAdmin,
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
    public List<TagResponse> getTagAdmin(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                         @RequestParam(value = "itemsPerPage", defaultValue = "10") int itemsPerPage) {
        return getTagsAdmin.getTagAdmin(pageNumber, itemsPerPage);
    }

    @DeleteMapping("/admin/tags")
    public Void deleteTag(@RequestParam(value = "tagId") String tagId) {
        return this.deleteTag.deleteTag(tagId);
    }
}
