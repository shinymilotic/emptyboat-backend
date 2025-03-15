package overcloud.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import overcloud.blog.usecase.user.admin_create_user.AdminCreateUserRequest;
import overcloud.blog.usecase.user.admin_create_user.IAdminCreateUser;
import overcloud.blog.usecase.user.common.UserResponse;
import overcloud.blog.usecase.user.confirm_email.ConfirmEmailRequest;
import overcloud.blog.usecase.user.confirm_email.ConfirmEmailService;
import overcloud.blog.usecase.user.delete_user.IDeleteUser;
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

import java.io.IOException;
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
    private final IDeleteUser deleteUser;

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
                          IAdminCreateUser adminCreateUser,
                          IDeleteUser deleteUser) {
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
        this.deleteUser = deleteUser;
    }

    @PostMapping("/users/register")
    public Void register(@RequestBody RegisterRequest registrationDto, HttpServletResponse response) {
        return registerService.registerUser(registrationDto, response);
    }

    @PutMapping("/users")
    public UserResponse update(@RequestBody UpdateUserRequest updateUserDto) {
        return updateUserService.updateUser(updateUserDto);
    }

    @PostMapping("/users/login")
    public UserResponse login(@RequestBody LoginRequest loginDto, HttpServletResponse response) {
        return loginService.login(loginDto, response);
    }

    @PostMapping("/users/logout")
    public Void logout(HttpServletRequest request, HttpServletResponse response) {
        return logoutService.logout(request, response);
    }

    @GetMapping("/users/current")
    public UserResponse getCurrentUser() {
        return getCurrentUserService.getCurrentUser();
    }

    @GetMapping("/profiles/{username}")
    public GetProfileResponse getProfile(@PathVariable String username) {
        return getProfileService.getProfile(username);
    }

    @PostMapping("/users/refresh-token")
    public UUID refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return refreshTokenService.refreshToken(request, response);
    }

    @GetMapping("/users")
    public List<UserResponse> getUsers(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(value = "itemsPerPage", defaultValue = "10") int itemsPerPage) {
        return getUserListService.getUsers(pageNumber, itemsPerPage);
    }

    @GetMapping("/users/count")
    public Long getUsersCount() {
        return getUserCountService.getUsersCount();
    }

    @GetMapping(ApiConst.ROLES_USERNAME)
    public List<UserRoleResponse> getRolesUser(@PathVariable String username) {
        return getRolesUserService.getRolesUser(username);
    }

    @PostMapping("/users/confirm-email")
    public Void confirmEmail(@RequestBody ConfirmEmailRequest confirmToken) {
        return confirmEmailService.confirmEmail(confirmToken);
    }

    @GetMapping("/followers/{userId}")
    public List<UserResponse> getFollowers(@PathVariable UUID userId) {
        return getFollowers.getFollowers(userId);
    }

    @GetMapping(ApiConst.SEARCHED_USERS)
    public Void searchedUsers(@PathVariable("keyword") String keyword) {
        return null;
    }

    @PostMapping("/admin/users")
    public Void adminCreateUser(@RequestBody AdminCreateUserRequest request) throws IOException {
        return adminCreateUser.adminCreateUser(request);
    }

    @DeleteMapping("/admin/users")
    public Void deleteUser(@PathVariable("userId") String userId) {
        return deleteUser.deleteUser(userId);
    }
}
