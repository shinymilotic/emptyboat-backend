package overcloud.blog.application.role;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.application.role.get_role.GetRoleUserService;
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
    private final GetRoleUserService getRoleUserService;

    public RoleController(ManageRoleService manageRoleService,
                          GetAllRoleService getAllRoleService,
                          GetRoleUserService getRoleUserService) {
        this.manageRoleService = manageRoleService;
        this.getAllRoleService = getAllRoleService;
        this.getRoleUserService = getRoleUserService;
    }

    @GetMapping(ApiConst.ROLES_USERNAME)
    public @ResponseBody RoleListResponse getRole(@PathVariable("username") String username) {
        return getRoleUserService.getRoleUserService(username);
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
