package overcloud.blog.usecase.user.get_followers;

import org.springframework.stereotype.Service;

import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.usecase.user.common.UserResponse;

import java.util.List;
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
    public RestResponse<List<UserResponse>> getFollowers(UUID userId) {
        return resFactory.success(UserResMsg.USER_GET_FOLLOWERS, userRepository.getFollowers(userId));
    }
}
