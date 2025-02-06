package overcloud.blog.usecase.blog.create_tag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.f4b6a3.uuid.UuidCreator;

import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ApiError;
import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
import overcloud.blog.utils.validation.ObjectsValidator;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.repository.ITagRepository;
import overcloud.blog.usecase.blog.common.TagResMsg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CreateTagsService {
    private final ITagRepository tagRepository;
    private final ObjectsValidator<CreateTagRequest> validator;
    private final ResFactory resFactory;

    public CreateTagsService(ITagRepository tagRepository,
                            ObjectsValidator<CreateTagRequest> validator,
                            ResFactory resFactory) {
        this.tagRepository = tagRepository;
        this.validator = validator;
        this.resFactory = resFactory;
    }

    @Transactional
    public List<String> createTags(CreateTagRequest createTagRequest) {
        Optional<ApiError> apiError = validator.validate(createTagRequest);
        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        List<String> tags = removeDuplicatedTags(createTagRequest.getTags());

        List<TagEntity> tagEntities = tagRepository.findByTagIds(createTagRequest.getTags());
        if (tagEntities.size() >= tags.size()) {
            throw new InvalidDataException(resFactory.fail(TagResMsg.TAG_EXISTS));
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
