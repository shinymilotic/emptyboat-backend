package overcloud.blog.application.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import overcloud.blog.application.user.get_current_user.CurrentUserResponse;
import overcloud.blog.application.user.get_current_user.GetCurrentUserService;
import overcloud.blog.application.user.get_profile.GetProfileResponse;
import overcloud.blog.application.user.get_profile.GetProfileService;
import overcloud.blog.application.user.login.LoginRequest;
import overcloud.blog.application.user.login.LoginService;
import overcloud.blog.application.user.logout.LogoutService;
import overcloud.blog.application.user.register.RegisterService;
import overcloud.blog.application.user.update_user.UpdateUserRequest;
import overcloud.blog.application.user.login.LoginResponse;
import overcloud.blog.application.user.register.RegisterResponse;
import overcloud.blog.application.user.update_user.UpdateUserResponse;
import overcloud.blog.application.user.register.RegisterRequest;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.application.user.update_user.UpdateUserService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserController {

    private final RegisterService registerService;
    private final UpdateUserService updateUserService;
    private final LogoutService logoutService;
    private final LoginService loginService;
    private final GetProfileService getProfileService;
    private final GetCurrentUserService getCurrentUserService;


    public UserController(RegisterService registerService,
                          UpdateUserService updateUserService,
                          LogoutService logoutService,
                          LoginService loginService,
                          GetProfileService getProfileService,
                          GetCurrentUserService getCurrentUserService) {
        this.registerService = registerService;
        this.updateUserService = updateUserService;
        this.logoutService = logoutService;
        this.loginService = loginService;
        this.getProfileService = getProfileService;
        this.getCurrentUserService = getCurrentUserService;
    }

    @PostMapping("users")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest registrationDto) throws Exception {
        return registerService.registerUser(registrationDto);
    }

    @PutMapping("user")
    public UpdateUserResponse update(@Valid @RequestBody UpdateUserRequest updateUserDto)  {
        return updateUserService.updateUser(updateUserDto);
    }

    @PostMapping("users/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginDto)  {
        return loginService.login(loginDto);
    }

    @PostMapping("users/logout")
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        return logoutService.logout(request, response);
    }

    @GetMapping("user")
    public CurrentUserResponse getCurrentUser() {
        return getCurrentUserService.getCurrentUser();
    }

    @GetMapping(value = "profiles/{username}")
    public GetProfileResponse getProfile(@PathVariable("username") String username) throws Exception {
        return getProfileService.getProfile(username);
    }
}
