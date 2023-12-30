package overcloud.blog.controller;

import org.springframework.web.bind.annotation.*;

import overcloud.blog.infrastructure.ApiConst;
import overcloud.blog.usecase.role.core.RoleListResponse;
import overcloud.blog.usecase.role.get_all_roles.GetAllRoleService;
import overcloud.blog.usecase.role.manage_role.ManageRoleRequest;
import overcloud.blog.usecase.role.manage_role.ManageRoleResponse;
import overcloud.blog.usecase.role.manage_role.ManageRoleService;

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

    @GetMapping(ApiConst.ROLES)
    public RoleListResponse getRoles() {
        return getAllRoleService.getRoles();
    }

    @PutMapping(ApiConst.ROLES)
    public ManageRoleResponse manageRole(@RequestBody ManageRoleRequest request) {
        return manageRoleService.manageRole(request);
    }
}
