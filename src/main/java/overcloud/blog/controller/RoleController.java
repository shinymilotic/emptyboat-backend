package overcloud.blog.controller;

import org.springframework.web.bind.annotation.*;

import overcloud.blog.usecase.user.get_all_roles.GetAllRoleService;
import overcloud.blog.usecase.user.manage_role.ManageRoleService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class RoleController {
    private final ManageRoleService manageRoleService;
    private final GetAllRoleService getAllRoleService;

    public RoleController(ManageRoleService manageRoleService,
                          GetAllRoleService getAllRoleService) {
        this.manageRoleService = manageRoleService;
        this.getAllRoleService = getAllRoleService;
    }

    // @GetMapping(ApiConst.ROLES)
    // public RestResponse<RoleListResponse> getRoles() {
    //     return getAllRoleService.getRoles();
    // }

    // @PutMapping(ApiConst.ROLES)
    // public RestResponse<ManageRoleResponse> manageRole(@RequestBody ManageRoleRequest request) {
    //     return manageRoleService.manageRole(request);
    // }
}
