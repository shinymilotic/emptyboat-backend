package overcloud.blog.usecase.admin.tag.delete_tag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.repository.ArticleTagRepository;
import overcloud.blog.repository.TagFollowRepository;
import overcloud.blog.repository.TagRepository;
import java.util.UUID;

@Service
public class DeleteTagImpl implements DeleteTag {
    private final TagRepository tagRepository;
    private final TagFollowRepository tagFollowRepository;
    private final ArticleTagRepository articleTagRepository;

    public DeleteTagImpl(TagRepository tagRepository,
                         TagFollowRepository tagFollowRepository,
                         ArticleTagRepository articleTagRepository) {
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
