package overcloud.blog.usecase.auth.get_all_roles;

import org.springframework.stereotype.Service;

import overcloud.blog.entity.RoleEntity;
import overcloud.blog.usecase.auth.common.RoleListResponse;
import overcloud.blog.usecase.auth.common.RoleMapper;
import overcloud.blog.usecase.auth.common.RoleRepository;

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

