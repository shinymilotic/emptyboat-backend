package overcloud.blog.application.tag.get_tags;

import org.springframework.stereotype.Service;
import overcloud.blog.application.tag.core.TagEntity;
import overcloud.blog.application.tag.core.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetTagsService {

    private final TagRepository tagRepository;

    public GetTagsService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public GetTagResponse getTags() {
        GetTagResponse tagResponse = new GetTagResponse();
        List<TagEntity> tagEntities = tagRepository.findAll();
        List<String> tags = new ArrayList<>();

        for (TagEntity tagEntity : tagEntities) {
            tags.add(tagEntity.getName());
        }

        tagResponse.setTagList(tags);

        return tagResponse;
    }
}
