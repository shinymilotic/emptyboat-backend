package overcloud.blog.usecase.role.get_all_roles;

import org.springframework.stereotype.Service;

import overcloud.blog.entity.RoleEntity;
import overcloud.blog.usecase.role.RoleListResponse;
import overcloud.blog.usecase.role.core.RoleMapper;
import overcloud.blog.usecase.role.core.RoleRepository;

import java.util.List;

@Service
public class GetAllRoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public GetAllRoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public RoleListResponse getRoles() {
         List<RoleEntity> roles = roleRepository.findAll();

         return roleMapper.toRoleListResponse(roles);
    }
}

