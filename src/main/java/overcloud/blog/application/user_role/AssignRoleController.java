package overcloud.blog.application.user_role;

import org.springframework.web.bind.annotation.*;
import overcloud.blog.application.role.RolesRequest;
import overcloud.blog.application.user_role.assign_role.AssignRoleService;
import overcloud.blog.application.user_role.assign_role.RoleAssignmentResponse;
import overcloud.blog.infrastructure.ApiConst;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AssignRoleController {

    private final AssignRoleService assignRoleService;

    public AssignRoleController(AssignRoleService assignRoleService) {
        this.assignRoleService = assignRoleService;
    }

    @PutMapping(ApiConst.USERS_USERNAME_ASSIGNMENT)
    public RoleAssignmentResponse assignRole(@RequestBody RolesRequest request, @PathVariable("username") String username) {
        return assignRoleService.assignRole(request, username);
    }
}
