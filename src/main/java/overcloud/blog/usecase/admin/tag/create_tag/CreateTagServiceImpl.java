package overcloud.blog.usecase.admin.tag.create_tag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.f4b6a3.uuid.UuidCreator;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ApiError;
import overcloud.blog.utils.validation.ObjectsValidator;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.repository.TagRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CreateTagServiceImpl implements CreateTagService {
    private final TagRepository tagRepository;
    private final ObjectsValidator<CreateTagRequest> validator;
    private static final String TAG_EXISTS = "admin.create-tag.tag-exists";

    public CreateTagServiceImpl(TagRepository tagRepository,
                                ObjectsValidator<CreateTagRequest> validator) {
        this.tagRepository = tagRepository;
        this.validator = validator;
    }

    @Override
    @Transactional
    public List<String> createTags(CreateTagRequest createTagRequest) {
        Optional<ApiError> apiError = validator.validate(createTagRequest);
        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        List<String> tags = removeDuplicatedTags(createTagRequest.getTags());

        List<TagEntity> tagEntities = tagRepository.findByTagIds(createTagRequest.getTags());
        if (tagEntities.size() >= tags.size()) {
            throw validator.fail(TAG_EXISTS);
        }

        saveAllTags(tags);

        return tags;
    }

    public List<String> removeDuplicatedTags(List<String> tags) {
        return tags.stream().distinct().toList();
    }

    public void saveAllTags(Collection<String> tags) {
        List<TagEntity> tagForSave = new ArrayList<>();
        for (String tag : tags) {
            TagEntity tagEntity = new TagEntity();
            tagEntity.setTagId(UuidCreator.getTimeOrderedEpoch());
            tagEntity.setName(tag);
            tagForSave.add(tagEntity);
        }
        tagRepository.saveAll(tagForSave);
    }
}
