package overcloud.blog.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import overcloud.blog.response.RestResponse;
import overcloud.blog.usecase.blog.common.TagResponse;
import overcloud.blog.usecase.blog.create_tag.CreateTagRequest;
import overcloud.blog.usecase.blog.create_tag.CreateTagsService;
import overcloud.blog.usecase.blog.follow_tag.FollowTagService;
import overcloud.blog.usecase.blog.get_tags.GetTagsService;
import org.springframework.web.bind.annotation.GetMapping;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TagController {
    private final CreateTagsService createTagService;
    private final GetTagsService getTagsService;
    private final FollowTagService followTagService;

    public TagController(CreateTagsService createTagService,
                         GetTagsService getTagsService,
                         FollowTagService followTagService) {
        this.createTagService = createTagService;
        this.getTagsService = getTagsService;
        this.followTagService = followTagService;
    }

    @PostMapping(ApiConst.TAGS)
    public RestResponse<List<String>> createTags(@RequestBody CreateTagRequest createTagRequest) {
        return createTagService.createTags(createTagRequest);
    }

    @GetMapping(ApiConst.TAGS)
    public RestResponse<List<TagResponse>> getTags() {
        return getTagsService.getTags();
    }

    @PostMapping(ApiConst.FOLLOW_TAG)
    public RestResponse<Void> followTag(@PathVariable("id") String tagId) {
        return followTagService.followTag(tagId);
    }
}
