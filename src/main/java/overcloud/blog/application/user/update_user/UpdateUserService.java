package overcloud.blog.application.user.update_user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserResponse;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

@Service
public class UpdateUserService {

    private UserRepository userRepository;

    private SpringAuthenticationService authenticationService;

    public UpdateUserService(UserRepository userRepository,
                           SpringAuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    public UpdateUserResponse updateUser(UpdateUserRequest updateUserDto) {
        String email = updateUserDto.getEmail();
        String updateBio = updateUserDto.getBio();
        String updateImage = updateUserDto.getImage();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .orElseThrow(EntityNotFoundException::new)
                .orElseThrow(EntityNotFoundException::new);

        if(!email.equals(currentUser.getEmail())) {
            throw new RuntimeException("Email not match current user");
        }

        currentUser.setBio(updateBio);
        currentUser.setImage(updateImage);

        UserEntity updateUserEntity = userRepository.save(currentUser);
        UpdateUserResponse updateUserResponse = new UpdateUserResponse();
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(updateUserEntity.getUsername());
        userResponse.setImage(updateUserEntity.getImage());
        userResponse.setBio(updateUserEntity.getBio());
        userResponse.setEmail(updateUserEntity.getEmail());
        updateUserResponse.setUserResponse(userResponse);

        return updateUserResponse;
    }
}
