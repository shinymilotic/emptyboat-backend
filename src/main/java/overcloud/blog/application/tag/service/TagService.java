package overcloud.blog.application.tag.service;

import overcloud.blog.application.tag.dto.create.CreateTagRequest;
import overcloud.blog.application.tag.dto.create.CreateTagResponse;
import overcloud.blog.application.tag.dto.get.GetTagResponse;
import overcloud.blog.application.tag.repository.TagRepository;
import overcloud.blog.domain.article.tag.TagEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public CreateTagResponse createTag(CreateTagRequest createTagRequest) throws Exception {
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
