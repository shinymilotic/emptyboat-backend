package overcloud.blog.usecase.tag.get_tags;

import org.springframework.stereotype.Service;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.usecase.tag.core.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetTagsService {

    private final TagRepository tagRepository;

    public GetTagsService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public GetTagResponse getTags() {
        List<String> tags = new ArrayList<>();
        List<TagEntity> tagEntities = tagRepository.findAll();

        for (TagEntity tagEntity : tagEntities) {
            tags.add(tagEntity.getName());
        }

        return toGetTagReponse(tags);
    }

    public GetTagResponse toGetTagReponse(List<String> tags) {
        return GetTagResponse.builder()
                .tagList(tags)
                .build();
    }
}
