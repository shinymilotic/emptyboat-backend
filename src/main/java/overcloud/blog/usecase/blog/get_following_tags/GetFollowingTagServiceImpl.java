package overcloud.blog.usecase.blog.get_following_tags;

import java.util.List;
import org.springframework.stereotype.Service;
import overcloud.blog.response.RestResponse;
import overcloud.blog.usecase.blog.common.ITagResponse;

@Service
public class GetFollowingTagServiceImpl implements GetFollowingTagService {

    @Override
    public RestResponse<List<ITagResponse>> getFollowingTags() {
        
        throw new UnsupportedOperationException("Unimplemented method 'getFollowingTags'");
    }
    
}
