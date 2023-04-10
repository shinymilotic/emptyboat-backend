package overcloud.blog.application.tag.controller;

import org.springframework.web.bind.annotation.*;
import overcloud.blog.application.tag.dto.create.CreateTagRequest;
import overcloud.blog.application.tag.dto.create.CreateTagResponse;
import overcloud.blog.application.tag.dto.get.GetTagResponse;
import overcloud.blog.application.tag.service.TagService;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/tags")
    public @ResponseBody CreateTagResponse createTag(@Valid @RequestBody CreateTagRequest createTagRequest) throws Exception {
        return tagService.createTag(createTagRequest);
    }

    @GetMapping(value = "/tags")
    public GetTagResponse getTags() {
        return tagService.getTags();
    }
}
