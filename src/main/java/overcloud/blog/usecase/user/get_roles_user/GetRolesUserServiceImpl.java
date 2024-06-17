package overcloud.blog.usecase.user.get_roles_user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.entity.RoleEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.jparepository.JpaRoleRepository;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.user.common.UserError;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetRolesUserServiceImpl implements GetRolesUserService {

    private final JpaUserRepository userRepository;

    private final JpaRoleRepository roleRepository;

    public GetRolesUserServiceImpl(JpaUserRepository userRepository,
                                   JpaRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserRoleListResponse getRolesUser(String username) {
        UserEntity user = userRepository.findRolesByUsernname(username);
        List<RoleEntity> roles = roleRepository.findAll();

        if (user == null) {
            // do something
            throw new InvalidDataException(UserError.USER_NOT_FOUND);
        }
        Set<RoleEntity> rolesUser = user.getRoles();

        List<UserRoleResponse> listResponse = new ArrayList<>();
        for (RoleEntity role : roles) {
            UserRoleResponse userRoleResponse =
                    new UserRoleResponse(role.getId().toString(), role.getName(), false);
            if (rolesUser.contains(role)) {
                userRoleResponse.setChecked(true);
            }
            listResponse.add(userRoleResponse);
        }

        UserRoleListResponse rs = new UserRoleListResponse(listResponse);
        return rs;
    }
}
