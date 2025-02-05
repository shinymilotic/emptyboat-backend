package overcloud.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import overcloud.blog.response.RestResponse;
import overcloud.blog.usecase.user.admin_create_user.AdminCreateUserRequest;
import overcloud.blog.usecase.user.admin_create_user.IAdminCreateUser;
import overcloud.blog.usecase.user.common.UserResponse;
import overcloud.blog.usecase.user.confirm_email.ConfirmEmailRequest;
import overcloud.blog.usecase.user.confirm_email.ConfirmEmailService;
import overcloud.blog.usecase.user.get_current_user.GetCurrentUserService;
import overcloud.blog.usecase.user.get_followers.GetFollowers;
import overcloud.blog.usecase.user.get_profile.GetProfileResponse;
import overcloud.blog.usecase.user.get_profile.GetProfileService;
import overcloud.blog.usecase.user.get_roles_user.GetRolesUserService;
import overcloud.blog.usecase.user.get_roles_user.UserRoleResponse;
import overcloud.blog.usecase.user.get_users.GetUserListService;
import overcloud.blog.usecase.user.get_users_count.IGetUserCountService;
import overcloud.blog.usecase.user.login.LoginRequest;
import overcloud.blog.usecase.user.login.LoginService;
import overcloud.blog.usecase.user.logout.LogoutService;
import overcloud.blog.usecase.user.refresh_token.RefreshTokenService;
import overcloud.blog.usecase.user.register.RegisterRequest;
import overcloud.blog.usecase.user.register.RegisterService;
import overcloud.blog.usecase.user.update_user.UpdateUserRequest;
import overcloud.blog.usecase.user.update_user.UpdateUserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserController {
    private final RegisterService registerService;
    private final UpdateUserService updateUserService;
    private final LogoutService logoutService;
    private final LoginService loginService;
    private final GetProfileService getProfileService;
    private final GetCurrentUserService getCurrentUserService;
    private final RefreshTokenService refreshTokenService;
    private final GetUserListService getUserListService;
    private final GetRolesUserService getRolesUserService;
    private final ConfirmEmailService confirmEmailService;
    private final GetFollowers getFollowers;
    private final IGetUserCountService getUserCountService;
    private final IAdminCreateUser adminCreateUser;

    public UserController(RegisterService registerService,
                          UpdateUserService updateUserService,
                          LogoutService logoutService,
                          LoginService loginService,
                          GetProfileService getProfileService,
                          GetCurrentUserService getCurrentUserService,
                          RefreshTokenService refreshTokenService,
                          GetUserListService getUserListService,
                          GetRolesUserService getRolesUserService,
                          ConfirmEmailService confirmEmailService,
                          GetFollowers getFollowers,
                          IGetUserCountService getUserCountService,
                          IAdminCreateUser adminCreateUser) {
        this.registerService = registerService;
        this.updateUserService = updateUserService;
        this.logoutService = logoutService;
        this.loginService = loginService;
        this.getProfileService = getProfileService;
        this.getCurrentUserService = getCurrentUserService;
        this.refreshTokenService = refreshTokenService;
        this.getUserListService = getUserListService;
        this.getRolesUserService = getRolesUserService;
        this.confirmEmailService = confirmEmailService;
        this.getFollowers = getFollowers;
        this.getUserCountService = getUserCountService;
        this.adminCreateUser = adminCreateUser;
    }

    @PostMapping(ApiConst.CURRENT_USER)
    public Void register(@RequestBody RegisterRequest registrationDto, HttpServletResponse response) {
        return registerService.registerUser(registrationDto, response);
    }

    @PutMapping(ApiConst.CURRENT_USER)
    public UserResponse update(@RequestBody UpdateUserRequest updateUserDto) {
        return updateUserService.updateUser(updateUserDto);
    }

    @PostMapping(ApiConst.USERS_LOGIN)
    public UserResponse login(@RequestBody LoginRequest loginDto, HttpServletResponse response) {
        return loginService.login(loginDto, response);
    }

    @PostMapping(ApiConst.USERS_LOGIN_ADMIN)
    public UserResponse loginAdmin(@RequestBody LoginRequest loginDto, HttpServletResponse response) {
        return loginService.login(loginDto, response);
    }

    @PostMapping(ApiConst.USERS_LOGOUT)
    public Void logout(HttpServletRequest request, HttpServletResponse response) {
        return logoutService.logout(request, response);
    }

    @GetMapping(ApiConst.CURRENT_USER)
    public UserResponse getCurrentUser() {
        return getCurrentUserService.getCurrentUser();
    }

    @GetMapping(ApiConst.PROFILES_USERNAME)
    public GetProfileResponse getProfile(@PathVariable String username) {
        return getProfileService.getProfile(username);
    }

    @PostMapping(ApiConst.USERS_REFRESHTOKEN)
    public UUID refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return refreshTokenService.refreshToken(request, response);
    }

    @GetMapping(ApiConst.USERS)
    public List<UserResponse> getUsers(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(value = "itemsPerPage", defaultValue = "10") int itemsPerPage) {
        return getUserListService.getUsers(pageNumber, itemsPerPage);
    }

    @GetMapping(ApiConst.USERS_COUNT)
    public Long getUsersCount() {
        return getUserCountService.getUsersCount();
    }

    @GetMapping(ApiConst.ROLES_USERNAME)
    public List<UserRoleResponse> getRolesUser(@PathVariable String username) {
        return getRolesUserService.getRolesUser(username);
    }

    @PostMapping(ApiConst.CONFIRM_EMAIL)
    public Void confirmEmail(@RequestBody ConfirmEmailRequest confirmToken) {
        return confirmEmailService.confirmEmail(confirmToken);
    }

    @GetMapping(ApiConst.FOLLOWERS)
    public List<UserResponse> getFollowers(@PathVariable UUID userId) {
        return getFollowers.getFollowers(userId);
    }

    @GetMapping(ApiConst.SEARCHED_USERS)
    public RestResponse searchedUsers(@PathVariable("keyword") String keyword) {
        return null;
    }

    @PostMapping("/admin/users")
    public Void adminCreateUser(@RequestBody AdminCreateUserRequest request) {
        return adminCreateUser.adminCreateUser(request);
    }
}
