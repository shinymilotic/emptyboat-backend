package overcloud.blog.usecase.blog.delete_tag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.repository.ITagRepository;
import java.util.UUID;

@Service
public class DeleteTagImpl implements IDeleteTag {
    private final ITagRepository tagRepository;

    public DeleteTagImpl(ITagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public Void deleteTag(String tagId) {
        UUID uuidTagId = UUID.fromString(tagId);
        tagRepository.deleteTag(uuidTagId);
        return null;
    }
}
