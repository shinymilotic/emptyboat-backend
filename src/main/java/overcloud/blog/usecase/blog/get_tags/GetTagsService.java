package overcloud.blog.usecase.blog.get_tags;

import org.springframework.stereotype.Service;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.repository.jparepository.JpaTagRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetTagsService {

    private final JpaTagRepository tagRepository;

    public GetTagsService(JpaTagRepository tagRepository) {
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
