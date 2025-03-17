package overcloud.blog.usecase.admin.tag.get_tags;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.repository.TagRepository;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetTagAdminImpl implements GetTagList {
    private final TagRepository tagRepository;

    public GetTagAdminImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public TagListResponse getTagList(int pageNumber, int itemsPerPage) {
        List<TagEntity> tags = tagRepository.findTags(pageNumber, itemsPerPage);
        TagListResponse response = new TagListResponse();
        List<TagResponse> tagList = new ArrayList<>();
        for (TagEntity tag: tags) {
            TagResponse tagResponse = new TagResponse();
            tagResponse.setId(tag.getTagId().toString());
            tagResponse.setName(tag.getName());
            tagList.add(tagResponse);
        }
        response.setTags(tagList);
        response.setTagCount(tags.size());

        return response;
    }
}
