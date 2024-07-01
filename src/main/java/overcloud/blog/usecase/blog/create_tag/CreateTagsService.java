package overcloud.blog.usecase.blog.create_tag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.entity.TagEntity;
import overcloud.blog.repository.ITagRepository;
import overcloud.blog.usecase.blog.common.TagResMsg;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ApiError;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.common.validation.ObjectsValidator;
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
    public RestResponse<CreateTagResponse> createTags(CreateTagRequest createTagRequest) {
        Optional<ApiError> apiError = validator.validate(createTagRequest);
        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        List<String> tags = removeDuplicatedTags(createTagRequest.getTags());

        List<TagEntity> tagEntities = tagRepository.findByTagName(createTagRequest.getTags());
        if (tagEntities.size() >= tags.size()) {
            throw new InvalidDataException(resFactory.fail(TagResMsg.TAG_EXISTS));
        }

        saveAllTags(tags);

        return resFactory.success(TagResMsg.TAG_CREATE_SUCCESS, toCreateTagResponse(tags));
    }

    public List<String> removeDuplicatedTags(List<String> tags) {
        return tags.stream().distinct().toList();
    }

    public void saveAllTags(Collection<String> tags) {
        List<TagEntity> tagForSave = new ArrayList<>();
        for (String tag : tags) {
            TagEntity tagEntity = new TagEntity();
            tagEntity.setName(tag);
            tagForSave.add(tagEntity);
        }
        tagRepository.saveAll(tagForSave);
    }

    public CreateTagResponse toCreateTagResponse(Iterable<String> tags) {
        return CreateTagResponse.builder()
                .tags(tags)
                .build();
    }
}
