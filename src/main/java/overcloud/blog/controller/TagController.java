package overcloud.blog.controller;

import org.springframework.web.bind.annotation.*;

import overcloud.blog.infrastructure.ApiConst;
import overcloud.blog.usecase.tag.create_tag.CreateTagRequest;
import overcloud.blog.usecase.tag.create_tag.CreateTagResponse;
import overcloud.blog.usecase.tag.create_tag.CreateTagsService;
import overcloud.blog.usecase.tag.get_tags.GetTagResponse;
import overcloud.blog.usecase.tag.get_tags.GetTagsService;

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
