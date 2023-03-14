package overcloud.blog.application.follow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import overcloud.blog.application.follow.dto.FollowResponse;
import overcloud.blog.application.follow.dto.UnfollowResponse;
import overcloud.blog.application.follow.repository.FollowRepository;
import overcloud.blog.application.user.repository.UserRepository;
import overcloud.blog.domain.user.UserEntity;
import overcloud.blog.domain.user.follow.FollowEntity;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpringAuthenticationService authenticationService;

    public FollowResponse follow(String username) {
        UserEntity securityUser = authenticationService.getCurrentUser().get().getUser().get();
        UserEntity followee = userRepository.findByUsername(username);

        FollowEntity followEntity = new FollowEntity();
        followEntity.setFollower(securityUser);
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
