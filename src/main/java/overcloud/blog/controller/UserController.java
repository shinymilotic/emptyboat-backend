package overcloud.blog.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.usecase.auth.common.AuthResponse;
import overcloud.blog.usecase.auth.common.UserListResponse;
import overcloud.blog.usecase.auth.common.UserResponse;
import overcloud.blog.usecase.auth.get_current_user.GetCurrentUserService;
import overcloud.blog.usecase.auth.get_profile.GetProfileResponse;
import overcloud.blog.usecase.auth.get_profile.GetProfileService;
import overcloud.blog.usecase.auth.get_roles_user.GetRolesUserService;
import overcloud.blog.usecase.auth.get_roles_user.UserRoleListResponse;
import overcloud.blog.usecase.auth.get_users.GetUserListService;
import overcloud.blog.usecase.auth.login.LoginRequest;
import overcloud.blog.usecase.auth.login.LoginService;
import overcloud.blog.usecase.auth.logout.LogoutService;
import overcloud.blog.usecase.auth.refresh_token.RefreshTokenRequest;
import overcloud.blog.usecase.auth.refresh_token.RefreshTokenService;
import overcloud.blog.usecase.auth.refresh_token.TokenRefreshResponse;
import overcloud.blog.usecase.auth.register.RegisterRequest;
import overcloud.blog.usecase.auth.register.RegisterService;
import overcloud.blog.usecase.auth.update_user.UpdateUserRequest;
import overcloud.blog.usecase.auth.update_user.UpdateUserService;

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

    public UserController(RegisterService registerService,
                          UpdateUserService updateUserService,
                          LogoutService logoutService,
                          LoginService loginService,
                          GetProfileService getProfileService,
                          GetCurrentUserService getCurrentUserService,
                          RefreshTokenService refreshTokenService,
                          GetUserListService getUserListService,
                          GetRolesUserService getRolesUserService) {
        this.registerService = registerService;
        this.updateUserService = updateUserService;
        this.logoutService = logoutService;
        this.loginService = loginService;
        this.getProfileService = getProfileService;
        this.getCurrentUserService = getCurrentUserService;
        this.refreshTokenService = refreshTokenService;
        this.getUserListService = getUserListService;
        this.getRolesUserService = getRolesUserService;
    }

    @PostMapping(ApiConst.USERS)
    public AuthResponse register(@RequestBody RegisterRequest registrationDto) throws Exception {
        return registerService.registerUser(registrationDto);
    }

    @PutMapping(ApiConst.USERS)
    public UserResponse update(@RequestBody UpdateUserRequest updateUserDto) {
        return updateUserService.updateUser(updateUserDto);
    }

    @PostMapping(ApiConst.USERS_LOGIN)
    public AuthResponse login(@RequestBody LoginRequest loginDto, HttpServletResponse response) {

        return loginService.login(loginDto, response);
    }

    @PostMapping(ApiConst.USERS_LOGIN_ADMIN)
    public AuthResponse loginAdmin(@RequestBody LoginRequest loginDto, HttpServletResponse response) {
        return loginService.login(loginDto, response);
    }

    @PostMapping(ApiConst.USERS_LOGOUT)
    public boolean logout(HttpServletRequest request, HttpServletResponse response, @RequestBody String refreshToken) {
        return logoutService.logout(request, response, refreshToken);
    }

    @GetMapping(ApiConst.USERS)
    public UserResponse getCurrentUser() {
        return getCurrentUserService.getCurrentUser();
    }

    @GetMapping(ApiConst.PROFILES_USERNAME)
    public GetProfileResponse getProfile(@PathVariable String username) throws Exception {
        return getProfileService.getProfile(username);
    }

    @PostMapping(ApiConst.USERS_REFRESHTOKEN)
    public TokenRefreshResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.refreshToken(refreshTokenRequest);
    }

    @GetMapping(ApiConst.USER_LIST)
    public UserListResponse getUsers(int page, int size) throws Exception {
        return getUserListService.getUsers(page, size);
    }

    @GetMapping(ApiConst.ROLES_USERNAME)
    public UserRoleListResponse getRolesUser(@PathVariable String username) {
        return getRolesUserService.getRolesUser(username);
    }
}
