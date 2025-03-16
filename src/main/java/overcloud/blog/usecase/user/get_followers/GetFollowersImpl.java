package overcloud.blog.usecase.user.get_followers;

import org.springframework.stereotype.Service;

import overcloud.blog.repository.UserRepository;
import overcloud.blog.usecase.user.common.UserResponse;

import java.util.List;
import java.util.UUID;

@Service
public class GetFollowersImpl implements GetFollowers {
    private final UserRepository userRepository;

    public GetFollowersImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponse> getFollowers(UUID userId) {
        return userRepository.getFollowers(userId);
    }
}
