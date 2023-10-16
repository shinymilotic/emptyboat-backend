package overcloud.blog.application.user.get_roles_user;

import org.springframework.stereotype.Service;
import overcloud.blog.application.role.RoleListResponse;
import overcloud.blog.application.role.core.RoleEntity;
import overcloud.blog.application.role.core.RoleMapper;
import overcloud.blog.application.role.core.RoleRepository;
import overcloud.blog.application.role.core.RoleResponse;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetRolesUserServiceImpl implements  GetRolesUserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserRoleMapper mapper;
    public GetRolesUserServiceImpl(UserRepository userRepository,
                                   UserRoleMapper mapper,
                                   RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserRoleListResponse getRolesUser(String username) {
        UserEntity user = userRepository.findRolesByUsernname(username);
        List<RoleEntity> roles = roleRepository.findAll();

        if(user == null) {
            // do something
        }
        Set<RoleEntity> rolesUser =  user.getRoles();

        List<UserRoleResponse> listResponse = new ArrayList<>();
        for (RoleEntity role: roles) {
            UserRoleResponse userRoleResponse =
                    new UserRoleResponse(role.getId().toString(), role.getName(), false);
            if (rolesUser.contains(role)) {
                userRoleResponse.setChecked(true);
            }
            listResponse.add(userRoleResponse);
        }

        UserRoleListResponse rs =  new UserRoleListResponse(listResponse);
        return rs;
    }


}
