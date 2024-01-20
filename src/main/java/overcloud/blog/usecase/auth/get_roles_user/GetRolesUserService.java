package overcloud.blog.usecase.auth.get_roles_user;

import org.springframework.stereotype.Service;

@Service
public interface GetRolesUserService {
    UserRoleListResponse getRolesUser(String username);
}
