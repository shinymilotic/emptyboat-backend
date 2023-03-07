package overcloud.blog.application.user.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import overcloud.blog.application.follow.dto.UnfollowResponse;
import overcloud.blog.application.follow.repository.FollowRepository;
import overcloud.blog.application.user.dto.get.CurrentUserResponse;
import overcloud.blog.application.user.dto.get.FollowUserResponse;
import overcloud.blog.application.user.dto.get.GetProfileResponse;
import overcloud.blog.application.user.dto.login.LoginRequest;
import overcloud.blog.application.user.dto.login.LoginResponse;
import overcloud.blog.application.user.dto.update.UpdateUserRequest;
import overcloud.blog.application.user.dto.register.RegisterResponse;
import overcloud.blog.application.user.dto.update.UpdateUserResponse;
import overcloud.blog.application.user.dto.UserResponse;
import overcloud.blog.application.user.exception.login.LoginError;
import overcloud.blog.application.user.exception.register.RegisterError;
import overcloud.blog.application.user.service.UserService;
import overcloud.blog.application.user.repository.UserRepository;
import overcloud.blog.domain.user.follow.FollowEntity;
import overcloud.blog.domain.user.UserEntity;
import overcloud.blog.application.user.dto.register.RegisterRequest;
import overcloud.blog.infrastructure.exceptionhandling.dto.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.dto.ApiErrorDetail;
import overcloud.blog.infrastructure.exceptionhandling.dto.ApiValidationError;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.JwtUtils;

import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpringAuthenticationService authenticationService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private FollowRepository followRepository;

    @Override
    public RegisterResponse registerUser(RegisterRequest registrationDto) throws Exception {
        UserEntity userEntity = new UserEntity();
        String username = registrationDto.getUsername();
        String email = registrationDto.getEmail();
        String hashedPassword = authenticationService.encodePassword(registrationDto.getPassword());
        List<ApiValidationError> errors = new ArrayList<>();

        if(userRepository.findByUsername(username) != null) {
            ApiValidationError apiErrorDetail =
                    new ApiValidationError("registrationDto", "username"
                            ,username,RegisterError.USERNAME_EXIST.getValue());
            errors.add(apiErrorDetail);
        }

        if(userRepository.findByEmail(email) != null) {
            ApiValidationError apiErrorDetail =
                    new ApiValidationError("registrationDto", "email"
                            ,username,RegisterError.EMAIL_EXIST.getValue());
            errors.add(apiErrorDetail);
        }

        if(!errors.isEmpty()) {
            ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Register information invalid");
            for (ApiValidationError error: errors) {
                apiError.getApiErrorDetails().add(error);
            }
            throw new RegisterInfoExistException(apiError);
        }

        userEntity.setUsername(registrationDto.getUsername());
        userEntity.setEmail(registrationDto.getEmail());
        userEntity.setPassword(hashedPassword);
        UserEntity user = userRepository.save(userEntity);

        RegisterResponse registerResponse = new RegisterResponse();
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setUsername(user.getUsername());
        userResponse.setToken(jwtUtils.encode(username));
        userResponse.setBio(user.getBio());
        userResponse.setImage(user.getImage());
        registerResponse.setUserResponse(userResponse);

        return registerResponse;
    }

    @Override
    public UpdateUserResponse updateUser(UpdateUserRequest updateUserDto) {
        String email = updateUserDto.getEmail();
        String updateBio = updateUserDto.getBio();
        String updateImage = updateUserDto.getImage();
        SecurityUser securityUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new BadCredentialsException("user not found"));
        UserEntity user = securityUser.getUser().get();

        if(!email.equals(user.getEmail())) {
            throw new RuntimeException("Email not match current user");
        }

        user.setBio(updateBio);
        user.setImage(updateImage);

        UserEntity updateUserEntity = userRepository.save(user);
        UpdateUserResponse updateUserResponse = new UpdateUserResponse();
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(updateUserEntity.getUsername());
        userResponse.setImage(updateUserEntity.getImage());
        userResponse.setBio(updateUserEntity.getBio());
        userResponse.setEmail(updateUserEntity.getEmail());
        updateUserResponse.setUserResponse(userResponse);

        return updateUserResponse;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String hashedPassword = loginRequest.getPassword();
        SecurityUser securityUser = authenticationService.authenticate(email, hashedPassword)
                // build exception
                .orElseThrow(() -> new EntityNotFoundException(LoginError.LOGIN_INFO_INVALID.getValue()));
        UserEntity user = securityUser.getUser().get();
        String token = jwtUtils.encode(email);

        LoginResponse loginResponse = new LoginResponse();
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(securityUser.getUsername());
        userResponse.setToken(token);
        userResponse.setImage(user.getImage());
        userResponse.setBio(user.getBio());
        userResponse.setEmail(user.getEmail());
        loginResponse.setUserResponse(userResponse);

        return loginResponse;
    }

    @Override
    public CurrentUserResponse getCurrentUser() {
        SecurityUser securityUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new BadCredentialsException("user not found"));
        UserEntity user = securityUser.getUser().get();
        CurrentUserResponse currentUserResponse = new CurrentUserResponse();
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setBio(user.getBio());
        userResponse.setImage(user.getImage());
        currentUserResponse.setUserResponse(userResponse);

        return currentUserResponse;
    }

    @Override
    public GetProfileResponse getProfile(String username) throws Exception {
        GetProfileResponse profileResponse = new GetProfileResponse();
        Optional<SecurityUser> securityUser = authenticationService.getCurrentUser();
        UserEntity currentUser = securityUser.get().getUser().get();
        String currentUsername = currentUser.getUsername();
        FollowEntity follow = followRepository.getFollowing(currentUsername, username);
        boolean isFollowing = false;
        if(securityUser.isPresent() || follow != null) {
            isFollowing = true;
        }

        UserEntity user = userRepository.findByUsername(username);
        if(user == null) {
            throw new Exception("User not found");
        }

        profileResponse.setUsername(user.getUsername());
        profileResponse.setFollowing(isFollowing);
        profileResponse.setBio(user.getBio());
        profileResponse.setImage(user.getImage());

        return profileResponse;
    }

    @Override
    public FollowUserResponse followUser(String username) {
        FollowUserResponse followUserResponse = new FollowUserResponse();
        FollowEntity followEntity = new FollowEntity();
        Optional<SecurityUser> securityUser = authenticationService.getCurrentUser();
        UserEntity currentUser = securityUser.get().getUser().get();
        UserEntity followee = userRepository.findByUsername(username);

        followEntity.setFollower(currentUser);
        followEntity.setFollowee(followee);
        followRepository.save(followEntity);

        followUserResponse.setUsername(followee.getUsername());
        followUserResponse.setBio(followee.getBio());
        followUserResponse.setImage(followee.getImage());
        followUserResponse.setFollowing(true);

        return followUserResponse;
    }

    @Override
    public UnfollowResponse unfollowUser(String username) {
        UnfollowResponse unfollowResponse = new UnfollowResponse();
        Optional<SecurityUser> securityUser = authenticationService.getCurrentUser();
        UserEntity followee = userRepository.findByUsername(username);
        FollowEntity followEntity = new FollowEntity();
        UserEntity currentUser = securityUser.get().getUser().get();

        followEntity.setFollower(currentUser);
        followEntity.setFollowee(followee);
        followRepository.delete(followEntity);

        unfollowResponse.setUsername(followee.getUsername());
        unfollowResponse.setBio(followee.getBio());
        unfollowResponse.setImage(followee.getImage());
        unfollowResponse.setFollowing(false);
        
        return unfollowResponse;
    }

    @Override
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return true;
    }


}

