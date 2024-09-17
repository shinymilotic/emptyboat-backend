package overcloud.blog.usecase.user.get_roles_user;

import java.util.List;

import org.springframework.stereotype.Service;

import overcloud.blog.response.RestResponse;

@Service
public interface GetRolesUserService {
    RestResponse<List<UserRoleResponse>> getRolesUser(String username);
}
