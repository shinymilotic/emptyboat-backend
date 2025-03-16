package overcloud.blog.usecase.user.get_roles_user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.entity.RoleEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.RoleRepository;
import overcloud.blog.repository.UserRepository;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetRolesUserServiceImpl implements GetRolesUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ObjectsValidator validator;

    public GetRolesUserServiceImpl(UserRepository userRepository,
                                   RoleRepository roleRepository,
                                   ObjectsValidator validator) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.validator = validator;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponse> getRolesUser(String username) {
        UserEntity user = userRepository.findRolesByUsernname(username);
        List<RoleEntity> roles = roleRepository.findAll();

        if (user == null) {
            throw validator.fail(UserResMsg.USER_NOT_FOUND);
        }
        Set<RoleEntity> rolesUser = user.getRoles();

        List<UserRoleResponse> listResponse = new ArrayList<>();
        for (RoleEntity role : roles) {
            UserRoleResponse userRoleResponse =
                    new UserRoleResponse(role.getRoleId().toString(), role.getName(), false);
            if (rolesUser.contains(role)) {
                userRoleResponse.setChecked(true);
            }
            listResponse.add(userRoleResponse);
        }

        return listResponse;
    }
}
