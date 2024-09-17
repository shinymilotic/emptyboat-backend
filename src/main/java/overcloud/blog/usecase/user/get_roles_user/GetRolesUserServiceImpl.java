package overcloud.blog.usecase.user.get_roles_user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.common.exceptionhandling.InvalidDataException;
import overcloud.blog.common.response.ResFactory;
import overcloud.blog.common.response.RestResponse;
import overcloud.blog.entity.RoleEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IRoleRepository;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.usecase.user.common.UserResMsg;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetRolesUserServiceImpl implements GetRolesUserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final ResFactory resFactory;

    public GetRolesUserServiceImpl(IUserRepository userRepository,
                                    IRoleRepository roleRepository,
                                    ResFactory resFactory) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.resFactory = resFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public RestResponse<List<UserRoleResponse>> getRolesUser(String username) {
        UserEntity user = userRepository.findRolesByUsernname(username);
        List<RoleEntity> roles = roleRepository.findAll();

        if (user == null) {
            throw new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND));
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

        return resFactory.success(UserResMsg.GET_ROLE_USER, (listResponse));
    }
}
