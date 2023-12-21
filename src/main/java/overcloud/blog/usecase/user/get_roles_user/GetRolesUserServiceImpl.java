package overcloud.blog.usecase.user.get_roles_user;

import org.springframework.stereotype.Service;
import overcloud.blog.entity.RoleEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.repository.UserRepository;
import overcloud.blog.usecase.role.core.RoleRepository;
import overcloud.blog.usecase.user.core.UserError;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetRolesUserServiceImpl implements  GetRolesUserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public GetRolesUserServiceImpl(UserRepository userRepository,
                                   RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserRoleListResponse getRolesUser(String username) {
        UserEntity user = userRepository.findRolesByUsernname(username);
        List<RoleEntity> roles = roleRepository.findAll();

        if(user == null) {
            // do something
            throw new InvalidDataException(UserError.USER_NOT_FOUND);
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
