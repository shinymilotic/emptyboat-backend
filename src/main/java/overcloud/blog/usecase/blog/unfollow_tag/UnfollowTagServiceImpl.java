package overcloud.blog.usecase.blog.unfollow_tag;

import org.springframework.stereotype.Service;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.TagFollowId;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.ITagFollowRepository;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;

import java.util.UUID;

@Service
public class UnfollowTagServiceImpl implements UnfollowTagService {
    private final ITagFollowRepository tagFollowRepository;
    private final SpringAuthenticationService authenticationService;
    private final ObjectsValidator validator;

    public UnfollowTagServiceImpl(ITagFollowRepository tagFollowRepository,
                                  SpringAuthenticationService authenticationService,
                                  ObjectsValidator validator) {
        this.tagFollowRepository = tagFollowRepository;
        this.authenticationService = authenticationService;
        this.validator = validator;
    }

    @Override
    public Void unfollowTag(String tagId) {
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> validator.fail(UserResMsg.USER_NOT_FOUND))
                .getUser();

        TagFollowId tagFollowId = new TagFollowId();
        tagFollowId.setTagId(UUID.fromString(tagId));
        tagFollowId.setFollowerId(currentUser.getUserId());
        tagFollowRepository.delete(tagFollowId);

        return null;
    }
}
