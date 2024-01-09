package overcloud.blog.controller;

import org.springframework.web.bind.annotation.*;

import overcloud.blog.infrastructure.ApiConst;
import overcloud.blog.usecase.role.core.RolesRequest;
import overcloud.blog.usecase.user_role.assign_role.AssignRoleService;
import overcloud.blog.usecase.user_role.assign_role.RoleAssignmentResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AssignRoleController {

    private final AssignRoleService assignRoleService;

    public AssignRoleController(AssignRoleService assignRoleService) {
        this.assignRoleService = assignRoleService;
    }

    @PutMapping(ApiConst.USERS_USERNAME_ASSIGNMENT)
    public RoleAssignmentResponse assignRole(@RequestBody RolesRequest request, @PathVariable String username) {
        return assignRoleService.assignRole(request, username);
    }
}
