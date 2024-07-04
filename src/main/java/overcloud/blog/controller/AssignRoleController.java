package overcloud.blog.controller;

import org.springframework.web.bind.annotation.*;

import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.user.assign_role.AssignRoleService;
import overcloud.blog.usecase.user.assign_role.RoleAssignmentResponse;
import overcloud.blog.usecase.user.common.RolesRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AssignRoleController {
    private final AssignRoleService assignRoleService;

    public AssignRoleController(AssignRoleService assignRoleService) {
        this.assignRoleService = assignRoleService;
    }

    @PutMapping(ApiConst.USERS_USERNAME_ASSIGNMENT)
    public RestResponse<RoleAssignmentResponse> assignRole(@RequestBody RolesRequest request, @PathVariable String username) {
        return assignRoleService.assignRole(request, username);
    }
}
