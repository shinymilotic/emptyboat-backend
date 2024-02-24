package overcloud.blog.usecase.auth.get_followers;

import org.springframework.stereotype.Service;
import overcloud.blog.repository.IUserRepository;

import java.util.UUID;

@Service
public class GetFollowersImpl implements GetFollowers {
    private final IUserRepository userRepository;

    public GetFollowersImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public FollowerListResposne getFollowers(UUID userId) {
        return userRepository.getFollowers(userId);
    }
}
