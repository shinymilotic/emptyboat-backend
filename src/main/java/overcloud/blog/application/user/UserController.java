package overcloud.blog.application.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import overcloud.blog.application.user.core.UserResponse;
import overcloud.blog.application.user.get_current_user.GetCurrentUserService;
import overcloud.blog.application.user.get_profile.GetProfileResponse;
import overcloud.blog.application.user.get_profile.GetProfileService;
import overcloud.blog.application.user.login.LoginRequest;
import overcloud.blog.application.user.login.LoginService;
import overcloud.blog.application.user.logout.LogoutService;
import overcloud.blog.application.user.register.RegisterService;
import overcloud.blog.application.user.update_user.UpdateUserRequest;
import overcloud.blog.application.user.login.LoginResponse;
import overcloud.blog.application.user.register.RegisterRequest;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.application.user.update_user.UpdateUserService;
import overcloud.blog.infrastructure.ApiConst;


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

    @PostMapping(ApiConst.USERS)
    public UserResponse register(@RequestBody RegisterRequest registrationDto) throws Exception {
        return registerService.registerUser(registrationDto);
    }

    @PutMapping(ApiConst.USERS)
    public UserResponse update(@RequestBody UpdateUserRequest updateUserDto)  {
        return updateUserService.updateUser(updateUserDto);
    }

    @PostMapping(ApiConst.USERS_LOGIN)
    public UserResponse login(@RequestBody LoginRequest loginDto)  {
        return loginService.login(loginDto);
    }

    @PostMapping(ApiConst.USERS_LOGOUT)
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        return logoutService.logout(request, response);
    }

    @GetMapping(ApiConst.USERS)
    public UserResponse getCurrentUser() {
        return getCurrentUserService.getCurrentUser();
    }

    @GetMapping(ApiConst.PROFILES_USERNAME)
    public GetProfileResponse getProfile(@PathVariable("username") String username) throws Exception {
        return getProfileService.getProfile(username);
    }
}
