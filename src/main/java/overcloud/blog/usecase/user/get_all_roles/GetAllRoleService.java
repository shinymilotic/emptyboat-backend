package overcloud.blog.usecase.user.get_all_roles;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.RoleEntity;
import overcloud.blog.repository.jparepository.JpaRoleRepository;
import overcloud.blog.usecase.user.common.RoleListResponse;
import overcloud.blog.usecase.user.common.RoleMapper;

import java.util.List;

@Service
public class GetAllRoleService {

    private final JpaRoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public GetAllRoleService(JpaRoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    // @Transactional(readOnly = true)
    // public RoleListResponse getRoles() {
    //     List<RoleEntity> roles = roleRepository.findAll();

    //     return roleMapper.toRoleListResponse(roles);
    // }
}

