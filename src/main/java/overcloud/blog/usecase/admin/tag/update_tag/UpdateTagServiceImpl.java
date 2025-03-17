package overcloud.blog.usecase.admin.tag.update_tag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.repository.TagRepository;
import overcloud.blog.utils.validation.ObjectsValidator;
import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateTagServiceImpl implements UpdateTag {
    private final TagRepository tagRepository;
    private final ObjectsValidator validator;
    public static final String TAG_NO_EXIST = "admin.update-tag.tag-no-exists";

    public UpdateTagServiceImpl(TagRepository tagRepository,
                                ObjectsValidator validator) {
        this.tagRepository = tagRepository;
        this.validator = validator;
    }

    @Override
    @Transactional
    public Void updateTag(UpdateTagRequest tag) {
        UUID tagId = UUID.fromString(tag.getTagId()) ;
        Optional<TagEntity> tagEntityOptional = tagRepository.findByTagId(tagId);

        if (tagEntityOptional.isEmpty()) {
            throw validator.fail(TAG_NO_EXIST);
        }

        tagRepository.save(tagEntityOptional.get());
        return null;
    }
}
