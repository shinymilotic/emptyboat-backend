package overcloud.blog.usecase.blog.create_tag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.validation.ObjectsValidator;
import overcloud.blog.usecase.blog.common.TagError;
import overcloud.blog.repository.jparepository.JpaTagRepository;

import java.util.*;

@Service
public class CreateTagsService {
    private final JpaTagRepository tagRepository;

    private final ObjectsValidator<CreateTagRequest> validator;

    public CreateTagsService(JpaTagRepository tagRepository, ObjectsValidator<CreateTagRequest> validator) {
        this.tagRepository = tagRepository;
        this.validator = validator;
    }

    @Transactional
    public CreateTagResponse createTags(CreateTagRequest createTagRequest) throws Exception {
        Optional<ApiError> apiError = validator.validate(createTagRequest);
        if (apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        List<String> tags = removeDuplicatedTags(createTagRequest.getTags());

        List<TagEntity> tagEntities = tagRepository.findByTagName(createTagRequest.getTags());
        if (tagEntities.size() >= tags.size()) {
            throw new InvalidDataException(ApiError.from(TagError.TAG_EXISTS));
        }

        saveAllTags(tags);

        return toCreateTagResponse(tags);
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
