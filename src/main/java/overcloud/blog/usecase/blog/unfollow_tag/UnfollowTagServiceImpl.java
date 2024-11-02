package overcloud.blog.usecase.blog.unfollow_tag;

import org.springframework.stereotype.Service;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.TagFollowId;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.repository.ITagFollowRepository;
import overcloud.blog.repository.impl.TagFollowRepositoryImpl;
import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
import overcloud.blog.usecase.blog.common.TagResMsg;
import overcloud.blog.usecase.blog.create_comment.CreateCommentRequest;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;

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
    public RestResponse<Void> unfollowTag(String tagId) {
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();

        TagFollowId tagFollowId = new TagFollowId();
        tagFollowId.setTagId(UUID.fromString(tagId));
        tagFollowId.setFollowerId(currentUser.getUserId());
        tagFollowRepository.delete(tagFollowId);

        return resFactory.success(TagResMsg.TAGS_UNFOLLOW_SUCCESS, null);
    }
}
