package overcloud.blog.usecase.blog.delete_tag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.repository.IArticleTagRepository;
import overcloud.blog.repository.ITagFollowRepository;
import overcloud.blog.repository.ITagRepository;
import java.util.UUID;

@Service
public class DeleteTagImpl implements IDeleteTag {
    private final ITagRepository tagRepository;
    private final ITagFollowRepository tagFollowRepository;
    private final IArticleTagRepository articleTagRepository;

    public DeleteTagImpl(ITagRepository tagRepository,
                         ITagFollowRepository tagFollowRepository,
                         IArticleTagRepository articleTagRepository) {
        this.tagRepository = tagRepository;
        this.tagFollowRepository = tagFollowRepository;
        this.articleTagRepository = articleTagRepository;
    }

    @Override
    @Transactional
    public Void deleteTag(String tagId) {
        UUID uuidTagId = UUID.fromString(tagId);
        tagFollowRepository.deleteByTagId(uuidTagId);
        articleTagRepository.deleteByTagId(uuidTagId);
        tagRepository.deleteTag(uuidTagId);
        return null;
    }
}
