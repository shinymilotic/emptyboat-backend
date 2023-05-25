package overcloud.blog.application.tag.create_tag;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import overcloud.blog.application.article.core.exception.InvalidDataException;
import overcloud.blog.application.tag.core.TagEntity;
import overcloud.blog.application.tag.core.TagError;
import overcloud.blog.application.tag.core.repository.TagRepository;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.validation.ObjectsValidator;

import java.util.*;

@Service
public class CreateTagsService {
    private final TagRepository tagRepository;

    private final ObjectsValidator<CreateTagRequest> validator;

    public CreateTagsService(TagRepository tagRepository, ObjectsValidator<CreateTagRequest> validator) {
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
