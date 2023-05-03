package overcloud.blog.application.user.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import overcloud.blog.application.user.follow.dto.UnfollowResponse;
import overcloud.blog.application.user.follow.repository.FollowRepository;
import overcloud.blog.application.user.follow.utils.FollowUtils;
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
import overcloud.blog.application.user.exception.register.RegisterInfoExistException;
import overcloud.blog.application.user.service.UserService;
import overcloud.blog.application.user.repository.UserRepository;
import overcloud.blog.application.user.follow.FollowId;
import overcloud.blog.application.user.follow.FollowEntity;
import overcloud.blog.application.user.UserEntity;
import overcloud.blog.application.user.dto.register.RegisterRequest;
import overcloud.blog.infrastructure.exceptionhandling.dto.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.dto.ApiValidationError;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.JwtUtils;

import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private SpringAuthenticationService authenticationService;

    private JwtUtils jwtUtils;

    private FollowRepository followRepository;

    private FollowUtils followUtils;

    public UserServiceImpl(UserRepository userRepository,
                           SpringAuthenticationService authenticationService,
                           JwtUtils jwtUtils,
                           FollowRepository followRepository,
                           FollowUtils followUtils) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
        this.followRepository = followRepository;
        this.followUtils = followUtils;
    }

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
            ApiError apiError = ApiError.from("Register information invalid");
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
        userResponse.setToken(jwtUtils.encode(email));
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

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String hashedPassword = loginRequest.getPassword();
        SecurityUser securityUser = authenticationService.authenticate(email, hashedPassword)
                // build exception
                .orElseThrow(() -> new EntityNotFoundException(LoginError.LOGIN_INFO_INVALID.getValue()));
        UserEntity user = securityUser.getUser().
                                    orElseThrow(EntityNotFoundException::new);
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
        UserEntity currentUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .orElseThrow(EntityNotFoundException::new)
                .orElseThrow(EntityNotFoundException::new);
        CurrentUserResponse currentUserResponse = new CurrentUserResponse();
        UserResponse userResponse = new UserResponse();

        userResponse.setUsername(currentUser.getUsername());
        userResponse.setEmail(currentUser.getEmail());
        userResponse.setBio(currentUser.getBio());
        userResponse.setToken(jwtUtils.encode(currentUser.getEmail()));
        userResponse.setImage(currentUser.getImage());
        currentUserResponse.setUserResponse(userResponse);

        return currentUserResponse;
    }

    @Override
    public GetProfileResponse getProfile(String username) {
        GetProfileResponse profileResponse = new GetProfileResponse();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .orElseThrow(EntityNotFoundException::new)
                .orElseThrow(EntityNotFoundException::new);

        UserEntity user = userRepository.findByUsername(username);

        profileResponse.setUsername(user.getUsername());
        profileResponse.setFollowing(followUtils.isFollowing(currentUser, user));
        profileResponse.setBio(user.getBio());
        profileResponse.setImage(user.getImage());
        profileResponse.setFollowersCount(followUtils.getFollowingCount(user));

        return profileResponse;
    }

    @Override
    public FollowUserResponse followUser(String username) {
        FollowUserResponse followUserResponse = new FollowUserResponse();
        FollowEntity followEntity = new FollowEntity();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .orElseThrow(EntityNotFoundException::new)
                .orElseThrow(EntityNotFoundException::new);

        UserEntity followee = userRepository.findByUsername(username);
        FollowId followId = new FollowId();
        followId.setFolloweeId(followee.getId());
        followId.setFollowerId(currentUser.getId());
        followEntity.setId(followId);
        followEntity.setFollower(currentUser);
        followEntity.setFollowee(followee);
        followRepository.save(followEntity);

        followUserResponse.setUsername(followee.getUsername());
        followUserResponse.setBio(followee.getBio());
        followUserResponse.setImage(followee.getImage());
        followUserResponse.setFollowing(true);
        followUserResponse.setFollowersCount(followUtils.getFollowingCount(followee));

        return followUserResponse;
    }

    @Override
    public UnfollowResponse unfollowUser(String username) {
        UnfollowResponse unfollowResponse = new UnfollowResponse();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .orElseThrow(EntityNotFoundException::new)
                .orElseThrow(EntityNotFoundException::new);

        UserEntity followee = userRepository.findByUsername(username);

        List<FollowEntity> followEntity = followRepository.getFollowing(currentUser.getUsername(), followee.getUsername());
        followRepository.delete(followEntity.get(0));

        unfollowResponse.setUsername(followee.getUsername());
        unfollowResponse.setBio(followee.getBio());
        unfollowResponse.setImage(followee.getImage());
        unfollowResponse.setFollowing(false);
        unfollowResponse.setFollowersCount(followUtils.getFollowingCount(followee));

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

