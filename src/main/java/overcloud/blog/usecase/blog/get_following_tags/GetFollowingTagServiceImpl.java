package overcloud.blog.usecase.blog.get_following_tags;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.repository.ITagRepository;
import overcloud.blog.response.ResFactory;
import overcloud.blog.usecase.user.common.UserResMsg;

@Service
public class GetFollowingTagServiceImpl implements GetFollowingTagService {
    private final SpringAuthenticationService authenticationService;
    private final ResFactory resFactory;
    private final ITagRepository tagRepository;

    public GetFollowingTagServiceImpl(SpringAuthenticationService authenticationService,
                                     ITagRepository tagRepository,
                                     ResFactory resFactory) {
        this.authenticationService = authenticationService;
        this.tagRepository = tagRepository;
        this.resFactory = resFactory;
    }

    @Override
    public List<FollowingTagResponse> getFollowingTags() {
        List<FollowingTagResponse> response = new ArrayList<>();

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> resFactory.fail(UserResMsg.USER_NOT_FOUND))
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
