package overcloud.blog.usecase.blog.get_tags_admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.repository.ITagRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetTagsAdminImpl implements IGetTagsAdmin {
    private final ITagRepository tagRepository;

    public GetTagsAdminImpl(ITagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public List<TagResponse> getTagsAdmin(int pageNumber, int itemsPerPage) {
        List<TagEntity> tags = tagRepository.findTags(pageNumber, itemsPerPage);
        List<TagResponse> response = new ArrayList<>();

        for (TagEntity tag: tags) {
            TagResponse tagResponse = new TagResponse();
            tagResponse.setId(tag.getTagId().toString());
            tagResponse.setName(tag.getName());
            response.add(tagResponse);
        }

        return response;
    }
}
