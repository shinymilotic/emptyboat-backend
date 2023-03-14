package overcloud.blog.application.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import overcloud.blog.application.follow.dto.UnfollowResponse;
import overcloud.blog.application.user.dto.get.CurrentUserResponse;
import overcloud.blog.application.user.dto.get.FollowUserResponse;
import overcloud.blog.application.user.dto.get.GetProfileResponse;
import overcloud.blog.application.user.dto.login.LoginRequest;
import overcloud.blog.application.user.dto.update.UpdateUserRequest;
import overcloud.blog.application.user.dto.login.LoginResponse;
import overcloud.blog.application.user.dto.register.RegisterResponse;
import overcloud.blog.application.user.dto.update.UpdateUserResponse;
import overcloud.blog.application.user.service.UserService;
import overcloud.blog.application.user.dto.register.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("users")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest registrationDto) throws Exception {
        return userService.registerUser(registrationDto);
    }

    @PutMapping("users")
    public UpdateUserResponse update(@Valid @RequestBody UpdateUserRequest updateUserDto) throws Exception {
        return userService.updateUser(updateUserDto);
    }

    @PostMapping("users/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginDto) throws Exception {
        return userService.login(loginDto);
    }

    @PostMapping("users/logout")
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        return userService.logout(request, response);
    }

    @GetMapping("users")
    public CurrentUserResponse getCurrentUser() {
        return userService.getCurrentUser();
    }

    @GetMapping(value = "profiles/{username}")
    public GetProfileResponse getProfile(@PathVariable("username") String username) throws Exception {
        return userService.getProfile(username);
    }

    @PostMapping(value = "profiles/{username}/follow")
    public FollowUserResponse followUser(@PathVariable("username") String username) {
        return userService.followUser(username);
    }

    @DeleteMapping(value = "profiles/{username}/follow")
    public UnfollowResponse unfollowUser(@PathVariable("username") String username) {
        return userService.unfollowUser(username);
    }
}
