package overcloud.blog.application.follow.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import overcloud.blog.application.follow.dto.FollowResponse;
import overcloud.blog.application.follow.repository.FollowRepository;
import overcloud.blog.application.user.repository.UserRepository;
import overcloud.blog.domain.user.UserEntity;
import overcloud.blog.domain.user.follow.FollowEntity;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

@Service
public class FollowService {

    private final FollowRepository followRepository;

    private final UserRepository userRepository;

    private final SpringAuthenticationService authenticationService;

    public FollowService(FollowRepository followRepository,
                         UserRepository userRepository,
                         SpringAuthenticationService authenticationService) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    public FollowResponse follow(String username) {
        UserEntity currentUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .orElseThrow(EntityNotFoundException::new)
                .orElseThrow(EntityNotFoundException::new);

        UserEntity followee = userRepository.findByUsername(username);

        FollowEntity followEntity = new FollowEntity();
        followEntity.setFollower(currentUser);
        followEntity.setFollowee(followee);

        followRepository.save(followEntity);
        FollowResponse followResponse = new FollowResponse();
        followResponse.setUsername(followee.getUsername());
        followResponse.setImage(followee.getImage());
        followResponse.setEmail(followResponse.getEmail());
        followResponse.setBio(followResponse.getBio());

        return followResponse;
    }

}
