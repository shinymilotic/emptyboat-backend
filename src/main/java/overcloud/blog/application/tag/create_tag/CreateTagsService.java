package overcloud.blog.application.tag.create_tag;

import org.springframework.stereotype.Service;
import overcloud.blog.application.tag.core.TagEntity;
import overcloud.blog.application.tag.core.repository.TagRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CreateTagsService {

    private final TagRepository tagRepository;

    public CreateTagsService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public CreateTagResponse createTags(CreateTagRequest createTagRequest) throws Exception {
        CreateTagResponse createTagResponse = new CreateTagResponse();
        List<TagEntity> tagForSave = new ArrayList<>();
        List<TagEntity> tagEntities = tagRepository.findAll();
        Set<String> tags = new HashSet<>();
        createTagRequest.getTagList()
                .forEach(tags::add);

        for (TagEntity tagEntity : tagEntities) {
            if(tags.contains(tagEntity.getName())) {
                throw new Exception("tag exist");
            }
        }

        for (String tag : tags) {
            TagEntity tagEntity = new TagEntity();
            tagEntity.setName(tag);
            tagForSave.add(tagEntity);

        }

        tagRepository.saveAll(tagForSave);

        createTagResponse.setTagList(tags);

        return createTagResponse;
    }
}
