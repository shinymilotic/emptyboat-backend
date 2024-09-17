package overcloud.blog.usecase.blog.get_tags;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.repository.ITagRepository;
import overcloud.blog.usecase.blog.common.TagResMsg;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetTagsService {
    private final ITagRepository tagRepository;
    private final ResFactory resFactory;

    public GetTagsService(ITagRepository tagRepository, ResFactory resFactory) {
        this.tagRepository = tagRepository;
        this.resFactory = resFactory;
    }

    @Transactional(readOnly = true)
    public RestResponse<List<String>> getTags() {
        List<String> tags = new ArrayList<>();
        List<TagEntity> tagEntities = tagRepository.findAll();

        for (TagEntity tagEntity : tagEntities) {
            tags.add(tagEntity.getName());
        }

        return resFactory.success(TagResMsg.TAG_GET_SUCCESS, tags);
    }
}
