package overcloud.blog.usecase.blog.get_following_tags;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.TagRepository;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;

@Service
public class GetFollowingTagServiceImpl implements GetFollowingTagService {
    private final SpringAuthenticationService authenticationService;
    private final ObjectsValidator validator;
    private final TagRepository tagRepository;

    public GetFollowingTagServiceImpl(SpringAuthenticationService authenticationService,
                                     TagRepository tagRepository,
                                     ObjectsValidator validator) {
        this.authenticationService = authenticationService;
        this.tagRepository = tagRepository;
        this.validator = validator;
    }

    @Override
    public List<FollowingTagResponse> getFollowingTags() {
        List<FollowingTagResponse> response = new ArrayList<>();

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> validator.fail(UserResMsg.USER_NOT_FOUND))
                .getUser();

        List<TagEntity> followingTags = tagRepository.findFollowingTags(currentUser.getUserId());

        for (TagEntity tagEntity : followingTags) {
            FollowingTagResponse tag = new FollowingTagResponse();
            tag.setId(tagEntity.getTagId().toString());
            tag.setName(tagEntity.getName());
            response.add(tag);
        }
        
        return response;
    }
    
}
