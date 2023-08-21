package overcloud.blog.application.role;

import org.springframework.web.bind.annotation.*;
import overcloud.blog.application.role.manage_role.ManageRoleRequest;
import overcloud.blog.application.role.manage_role.ManageRoleResponse;
import overcloud.blog.application.role.manage_role.ManageRoleService;
import overcloud.blog.application.role.get_all_roles.GetAllRoleService;
import overcloud.blog.infrastructure.ApiConst;

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
