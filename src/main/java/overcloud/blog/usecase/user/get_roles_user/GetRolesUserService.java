package overcloud.blog.usecase.user.get_roles_user;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface GetRolesUserService {
    List<UserRoleResponse> getRolesUser(String username);
}
