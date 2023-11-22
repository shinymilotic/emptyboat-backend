package overcloud.blog.application.tag;

import org.springframework.web.bind.annotation.*;
import overcloud.blog.application.tag.create_tag.CreateTagRequest;
import overcloud.blog.application.tag.create_tag.CreateTagResponse;
import overcloud.blog.application.tag.create_tag.CreateTagsService;
import overcloud.blog.application.tag.get_tags.GetTagResponse;
import overcloud.blog.application.tag.get_tags.GetTagsService;
import overcloud.blog.infrastructure.ApiConst;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TagController {
    private final CreateTagsService createTagService;
    private final GetTagsService getTagsService;

    public TagController(CreateTagsService createTagService,
                         GetTagsService getTagsService) {
        this.createTagService = createTagService;
        this.getTagsService = getTagsService;
    }

    @PostMapping(ApiConst.TAGS)
    public CreateTagResponse createTags(@RequestBody CreateTagRequest createTagRequest) throws Exception {
        return createTagService.createTags(createTagRequest);
    }

    @GetMapping(ApiConst.TAGS)
    public GetTagResponse getTags() {
        return getTagsService.getTags();
    }
}
