package overcloud.blog.application.role.get_all_roles;

import org.springframework.stereotype.Service;
import overcloud.blog.application.role.RoleListResponse;
import overcloud.blog.application.role.core.RoleEntity;
import overcloud.blog.application.role.core.RoleRepository;
import overcloud.blog.application.role.core.RoleResponse;

import java.util.List;

@Service
public class GetAllRoleService {

    private final RoleRepository roleRepository;

    public GetAllRoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleListResponse getRoles() {
         List<RoleEntity> roles = roleRepository.findAll();

         return toRoleListResponse(roles);
    }

    private RoleListResponse toRoleListResponse(List<RoleEntity> roles) {
        List<RoleResponse> roleResponseList = roles.stream()
                .map(this::toRoleResponse)
                .toList();

        return RoleListResponse.builder()
                .roleList(roleResponseList)
                .build();
    }

    private RoleResponse toRoleResponse(RoleEntity roleEntity) {
        return RoleResponse.builder()
                .roleId(roleEntity.getId().toString())
                .roleName(roleEntity.getName())
                .build();
    }
}

