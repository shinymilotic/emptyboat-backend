package overcloud.blog.usecase.blog.get_tags;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.Tuple;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.TagRepository;
import overcloud.blog.usecase.blog.common.TagResponse;
import overcloud.blog.usecase.blog.common.TagFollowingResponse;
import overcloud.blog.usecase.blog.common.TagResponseSimple;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GetTagsServiceImpl implements GetTagsService {
    private final TagRepository tagRepository;
    private final ObjectsValidator validator;
    private final SpringAuthenticationService authenticationService;

    public GetTagsServiceImpl(TagRepository tagRepository,
                              ObjectsValidator validator,
                              SpringAuthenticationService authenticationService) {
        this.tagRepository = tagRepository;
        this.validator = validator;
        this.authenticationService = authenticationService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagResponse> getTags(Boolean following) {
        List<TagResponse> response = new ArrayList<>();

        if (following == null || !following) {
            List<TagEntity> tagEntities = tagRepository.findAll();
            for (TagEntity tagEntity : tagEntities) {
                TagResponseSimple tagResponse = new TagResponseSimple();
                tagResponse.setId(tagEntity.getTagId().toString());
                tagResponse.setName(tagEntity.getName());
                response.add(tagResponse);
            }
        } else {
            UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> validator.fail(UserResMsg.USER_NOT_FOUND))
                .getUser();

            List<Tuple> tags = tagRepository.findAllWithFollowing(currentUser.getUserId());
            for (Tuple tag : tags) {
                UUID tagId = (UUID) tag.get("tag_id");
                String tagName = (String) tag.get("name");
                UUID userId = (UUID) tag.get("user_id");
                TagFollowingResponse tagResponse = new TagFollowingResponse();
                tagResponse.setId(tagId.toString());
                tagResponse.setName(tagName);

                if (userId != null) {
                    tagResponse.setFollowing(true);
                } else {
                    tagResponse.setFollowing(false);
                }
                response.add(tagResponse);
            }
        }

        return response;
    }
}
