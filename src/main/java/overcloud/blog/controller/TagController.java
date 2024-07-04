package overcloud.blog.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.usecase.blog.create_tag.CreateTagRequest;
import overcloud.blog.usecase.blog.create_tag.CreateTagsService;
import overcloud.blog.usecase.blog.get_tags.GetTagsService;
import overcloud.blog.usecase.common.response.RestResponse;

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
    public RestResponse<List<String>> createTags(@RequestBody CreateTagRequest createTagRequest) {
        return createTagService.createTags(createTagRequest);
    }

    @GetMapping(ApiConst.TAGS)
    public RestResponse<List<String>> getTags() {
        return getTagsService.getTags();
    }
}
