package overcloud.blog.usecase.user.get_followers;

import org.springframework.stereotype.Service;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.user.common.UserResMsg;

import java.util.UUID;

@Service
public class GetFollowersImpl implements GetFollowers {
    private final IUserRepository userRepository;
    private final ResFactory resFactory;

    public GetFollowersImpl(IUserRepository userRepository, ResFactory resFactory) {
        this.userRepository = userRepository;
        this.resFactory = resFactory;
    }

    @Override
    public RestResponse<FollowerListResposne> getFollowers(UUID userId) {
        return resFactory.success(UserResMsg.USER_GET_FOLLOWERS, userRepository.getFollowers(userId));
    }
}
