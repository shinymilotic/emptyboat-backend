package overcloud.blog.controller;

import overcloud.blog.usecase.user.admin_create_user.AdminCreateUserRequest;
import overcloud.blog.usecase.user.admin_create_user.IAdminCreateUser;
import overcloud.blog.usecase.user.common.UserResponse;
import overcloud.blog.usecase.user.delete_user.IDeleteUser;
import overcloud.blog.usecase.user.get_users.GetUserListService;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AdminUserController {
    private final GetUserListService getUserListService;
    private final IAdminCreateUser adminCreateUser;
    private final IDeleteUser deleteUser;

    public AdminUserController(GetUserListService getUserListService, IAdminCreateUser adminCreateUser, IDeleteUser deleteUser) {
        this.getUserListService = getUserListService;
        this.adminCreateUser = adminCreateUser;
        this.deleteUser = deleteUser;
    }

    @GetMapping("/admin/users")
    public List<UserResponse> getUsers(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(value = "itemsPerPage", defaultValue = "10") int itemsPerPage) {
        return getUserListService.getUsers(pageNumber, itemsPerPage);
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
