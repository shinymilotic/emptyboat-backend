package overcloud.blog.usecase.blog.unfollow_tag;

import org.springframework.stereotype.Service;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.TagFollowId;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.repository.ITagFollowRepository;
import overcloud.blog.response.ResFactory;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.util.UUID;

@Service
public class UnfollowTagServiceImpl implements UnfollowTagService {
    private final ITagFollowRepository tagFollowRepository;
    private final SpringAuthenticationService authenticationService;
    private final ResFactory resFactory;

    public UnfollowTagServiceImpl(ITagFollowRepository tagFollowRepository,
                                  SpringAuthenticationService authenticationService,
                                  ResFactory resFactory) {
        this.tagFollowRepository = tagFollowRepository;
        this.authenticationService = authenticationService;
        this.resFactory = resFactory;
    }

    @Override
    public Void unfollowTag(String tagId) {
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();

        TagFollowId tagFollowId = new TagFollowId();
        tagFollowId.setTagId(UUID.fromString(tagId));
        tagFollowId.setFollowerId(currentUser.getUserId());
        tagFollowRepository.delete(tagFollowId);

        return null;
    }
}
