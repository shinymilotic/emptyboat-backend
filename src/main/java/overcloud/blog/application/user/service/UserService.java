package overcloud.blog.application.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import overcloud.blog.application.user.follow.dto.UnfollowResponse;
import overcloud.blog.application.user.dto.get.CurrentUserResponse;
import overcloud.blog.application.user.dto.get.FollowUserResponse;
import overcloud.blog.application.user.dto.get.GetProfileResponse;
import overcloud.blog.application.user.dto.login.LoginRequest;
import overcloud.blog.application.user.dto.login.LoginResponse;
import overcloud.blog.application.user.dto.register.RegisterRequest;
import overcloud.blog.application.user.dto.update.UpdateUserRequest;
import overcloud.blog.application.user.dto.register.RegisterResponse;
import overcloud.blog.application.user.dto.update.UpdateUserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    RegisterResponse registerUser(RegisterRequest registrationDto) throws Exception;

    UpdateUserResponse updateUser(UpdateUserRequest updateUserDto);

    LoginResponse login(LoginRequest loginRequest);

    CurrentUserResponse getCurrentUser();

    GetProfileResponse getProfile(String username) throws Exception;

    FollowUserResponse followUser(String username);

    UnfollowResponse unfollowUser(String username);

    boolean logout(HttpServletRequest request, HttpServletResponse response);
}
