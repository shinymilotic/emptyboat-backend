package overcloud.blog.controller;

import overcloud.blog.usecase.admin.user.create_user.CreateUserRequest;
import overcloud.blog.usecase.admin.user.create_user.CreateUser;
import overcloud.blog.usecase.admin.user.get_users.UserListResponse;
import overcloud.blog.usecase.user.common.UserResponse;
import overcloud.blog.usecase.admin.user.delete_user.DeleteUser;
import overcloud.blog.usecase.admin.user.get_users.GetUserListServiceImpl;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AdminUserController {
    private final GetUserListServiceImpl getUserListService;
    private final CreateUser createUser;
    private final DeleteUser deleteUser;

    public AdminUserController(GetUserListServiceImpl getUserListService,
                               CreateUser createUser,
                               DeleteUser deleteUser) {
        this.getUserListService = getUserListService;
        this.createUser = createUser;
        this.deleteUser = deleteUser;
    }

    @GetMapping("/admin/users")
    public UserListResponse getUsers(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(value = "itemsPerPage", defaultValue = "10") int itemsPerPage) {
        return getUserListService.getUsers(pageNumber, itemsPerPage);
    }

    @PostMapping("/admin/users")
    public Void createUser(@RequestBody CreateUserRequest request) throws IOException {
        return createUser.createUser(request);
    }

    @DeleteMapping("/admin/users")
    public Void deleteUser(@PathVariable("userId") String userId) {
        return deleteUser.deleteUser(userId);
    }
}
