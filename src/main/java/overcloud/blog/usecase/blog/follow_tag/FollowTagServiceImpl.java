package overcloud.blog.usecase.blog.follow_tag;

import java.util.UUID;
import org.springframework.stereotype.Service;

import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.TagFollowEntity;
import overcloud.blog.entity.TagFollowId;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.TagFollowRepository;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;

@Service
public class FollowTagServiceImpl implements FollowTagService {
    private final TagFollowRepository tagFollowRepository;
    private final SpringAuthenticationService authenticationService;
    private final ObjectsValidator validator;

    public FollowTagServiceImpl(TagFollowRepository tagFollowRepository,
                                SpringAuthenticationService authenticationService,
                                ObjectsValidator validator) {
        this.validator = validator;
        this.authenticationService = authenticationService;
        this.tagFollowRepository = tagFollowRepository;
    }

    @Override
    public Void followTag(String id) {
        UUID tagId = UUID.fromString(id);

        UserEntity currentUser = authenticationService.getCurrentUser()
            .orElseThrow(() -> validator.fail(UserResMsg.USER_NOT_FOUND))
            .getUser();

        TagFollowId tagFollowId = new TagFollowId();
        tagFollowId.setTagId(tagId);
        tagFollowId.setFollowerId(currentUser.getUserId());
        TagFollowEntity tagFollowEntity = new TagFollowEntity();
        tagFollowEntity.setTagFollowId(tagFollowId);
        tagFollowRepository.save(tagFollowEntity);

        return null;
    }
    
}
