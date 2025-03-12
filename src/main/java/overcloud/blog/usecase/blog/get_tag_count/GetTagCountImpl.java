package overcloud.blog.usecase.blog.get_tag_count;

import org.springframework.stereotype.Service;
import overcloud.blog.repository.ITagRepository;

@Service
public class GetTagCountImpl implements IGetTagCount {
    private final ITagRepository tagRepository;

    public GetTagCountImpl(ITagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Long getTagCount() {
        return tagRepository.getTagCount();
    }
}
